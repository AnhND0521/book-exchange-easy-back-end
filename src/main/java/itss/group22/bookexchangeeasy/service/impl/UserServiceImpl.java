package itss.group22.bookexchangeeasy.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import itss.group22.bookexchangeeasy.dto.user.*;
import itss.group22.bookexchangeeasy.entity.AddressUnit;
import itss.group22.bookexchangeeasy.entity.ContactInfo;
import itss.group22.bookexchangeeasy.entity.Role;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.enums.Gender;
import itss.group22.bookexchangeeasy.exception.ApiException;
import itss.group22.bookexchangeeasy.exception.ResourceNotFoundException;
import itss.group22.bookexchangeeasy.repository.AddressUnitRepository;
import itss.group22.bookexchangeeasy.repository.ContactInfoRepository;
import itss.group22.bookexchangeeasy.repository.RoleRepository;
import itss.group22.bookexchangeeasy.repository.UserRepository;
import itss.group22.bookexchangeeasy.service.CloudinaryService;
import itss.group22.bookexchangeeasy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressUnitRepository addressUnitRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final CloudinaryService cloudinaryService;

    @Value("${app.secret-key}")
    private String secretKey;

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ApiException("Account does not exist", HttpStatus.FORBIDDEN));
        log.info("Found user: " + user.getEmail());

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword()))
            throw new ApiException("Incorrect password", HttpStatus.FORBIDDEN);

        if (user.getIsLocked())
            throw new ApiException("User account is currently locked", HttpStatus.FORBIDDEN);

        String token = generateToken(user);
        log.info("Generated token");

        return AuthResponse.builder().accessToken(token).build();
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail()))
            throw new ApiException("Email already registered");

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setGender(Gender.valueOf(registerRequest.getGender()));
        user.setRoles(registerRequest.getRoles().stream().map(name ->
                roleRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException("role", "name", name))
        ).collect(Collectors.toSet()));

        if (registerRequest.getBirthDate().isAfter(LocalDate.now()))
            throw new ApiException("Invalid date of birth");
        user.setBirthDate(registerRequest.getBirthDate());

        user.setIsLocked(false);
        user.setIsVerified(false);

        AddressUnit province = null;
        AddressUnit district = null;
        AddressUnit commune = null;
        if (registerRequest.getProvinceId() != null)
            province = addressUnitRepository.findById(registerRequest.getProvinceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Province", "id", registerRequest.getProvinceId()));
        if (registerRequest.getDistrictId() != null)
            district = addressUnitRepository.findById(registerRequest.getDistrictId())
                    .orElseThrow(() -> new ResourceNotFoundException("District", "id", registerRequest.getDistrictId()));
        if (registerRequest.getCommuneId() != null)
            commune = addressUnitRepository.findById(registerRequest.getCommuneId())
                    .orElseThrow(() -> new ResourceNotFoundException("Commune", "id", registerRequest.getCommuneId()));

        ContactInfo contactInfo = ContactInfo.builder()
                .phoneNumber(registerRequest.getPhoneNumber())
                .province(province)
                .district(district)
                .commune(commune)
                .detailedAddress(registerRequest.getDetailedAddress())
                .build();
        contactInfo = contactInfoRepository.save(contactInfo);

        user.setContactInfo(contactInfo);
        userRepository.save(user);
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli()))
                .claim("id", user.getId())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) return "";
        var roles = user.getRoles().stream().map(Role::getName).toList();
        return String.join(" ", roles);
    }

    @Override
    public UserProfile getProfile(Long id) {
        User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return buildProfile(user);
    }

    @Override
    public void updateProfile(Long id, UserProfile userProfile) {
        User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        user.setName(userProfile.getName());
        user.setRoles(userProfile.getRoles().stream().map(name ->
                roleRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException("role", "name", name))
        ).collect(Collectors.toSet()));
        user.setGender(Gender.valueOf(userProfile.getGender()));

        if (userProfile.getBirthDate().isAfter(LocalDate.now()))
            throw new ApiException("Invalid date of birth");
        user.setBirthDate(userProfile.getBirthDate());

        if (userProfile.hasContactInfo()) {
            AddressUnit province = null, district = null, commune = null;
            if (userProfile.getProvince() != null && userProfile.getProvince().getId() != null)
                province = addressUnitRepository.findById(userProfile.getProvince().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Province", "id", userProfile.getProvince().getId()));
            if (userProfile.getDistrict() != null && userProfile.getDistrict().getId() != null)
                district = addressUnitRepository.findById(userProfile.getDistrict().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("District", "id", userProfile.getDistrict().getId()));
            if (userProfile.getCommune() != null && userProfile.getCommune().getId() != null)
                commune = addressUnitRepository.findById(userProfile.getCommune().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Commune", "id", userProfile.getCommune().getId()));

            ContactInfo contactInfo = ContactInfo.builder()
                    .phoneNumber(userProfile.getPhoneNumber())
                    .province(province)
                    .district(district)
                    .commune(commune)
                    .detailedAddress(userProfile.getDetailedAddress())
                    .build();

            if (!contactInfo.equals(user.getContactInfo())) {
                ContactInfo oldContactInfo = user.getContactInfo();
                contactInfoRepository.save(contactInfo);
                user.setContactInfo(contactInfo);
                userRepository.save(user);
                if (oldContactInfo != null)
                    contactInfoRepository.delete(oldContactInfo);
            } else {
                userRepository.save(user);
            }
        } else {
            userRepository.save(user);
        }
    }

    @Override
    public void changePassword(Long id, ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword()))
            throw new ApiException("Incorrect password", HttpStatus.BAD_REQUEST);
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public List<UserProfile> getUserList(int page, int size) {
        return userRepository.findAllByOrderByEmailAsc(PageRequest.of(page, size))
                .stream()
                .map(this::buildProfile)
                .toList();
    }

    @Override
    public List<UserProfile> searchUser(String keyword, int page, int size) {
        return userRepository.findByEmailOrName(keyword, PageRequest.of(page, size))
                .stream()
                .map(this::buildProfile)
                .toList();
    }

    @Override
    public void lockUserAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (user.getIsLocked())
            throw new ApiException("User account is already locked");

        user.setIsLocked(true);
        userRepository.save(user);
    }

    @Override
    public void unlockUserAccount(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (!user.getIsLocked())
            throw new ApiException("User account is not being locked");

        user.setIsLocked(false);
        userRepository.save(user);
    }

    @Override
    public String uploadAvatar(Long id, MultipartFile imageFile) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        Map data =  cloudinaryService.uploadFile(imageFile);
        user.setPictureUrl(data.get("url").toString());
        return data.get("url").toString();
    }

    private UserProfile buildProfile(User user) {
        UserProfile userProfile = mapper.map(user, UserProfile.class);
        userProfile.setGender(user.getGender().name());
        userProfile.setRoles(user.getRoles().stream().map(Role::getName).toList());

        if (user.getContactInfo() != null) {
            userProfile.setPhoneNumber(user.getContactInfo().getPhoneNumber());

            if (user.getContactInfo().getProvince() != null)
                userProfile.setProvince(AddressUnitDTO.builder()
                        .id(user.getContactInfo().getProvince().getId()).
                        name(user.getContactInfo().getProvince().getName()).build());

            if (user.getContactInfo().getDistrict() != null)
                userProfile.setDistrict(AddressUnitDTO.builder()
                        .id(user.getContactInfo().getDistrict().getId()).
                        name(user.getContactInfo().getDistrict().getName()).build());

            if (user.getContactInfo().getCommune() != null)
                userProfile.setCommune(AddressUnitDTO.builder()
                        .id(user.getContactInfo().getCommune().getId()).
                        name(user.getContactInfo().getCommune().getName()).build());

            userProfile.setDetailedAddress(user.getContactInfo().getDetailedAddress());
        }

        return userProfile;
    }
}
