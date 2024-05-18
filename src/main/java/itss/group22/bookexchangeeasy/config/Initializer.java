package itss.group22.bookexchangeeasy.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import itss.group22.bookexchangeeasy.entity.ContactInfo;
import itss.group22.bookexchangeeasy.entity.Role;
import itss.group22.bookexchangeeasy.entity.User;
import itss.group22.bookexchangeeasy.enums.Gender;
import itss.group22.bookexchangeeasy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Initializer {
    private final DataSource dataSource;
    private final AddressUnitRepository addressUnitRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BookRepository bookRepository;
    private final ExchangeRequestRepository exchangeRequestRepository;
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
            generateUsers(50);
            if (addressUnitRepository.count() == 0) {
                importSql("data/address_unit.sql");
            }
//            if (contactInfoRepository.count() == 0) {
//                importSql("data/contact_info.sql");
//            }
            if (userRepository.count() == 0) {
//                importSql("data/user_info.sql", "data/role.sql", "data/users_roles.sql");
                generateUsers(50);
            }
            if (bookRepository.count() == 0) {
                importSql("data/book.sql", "data/money_item.sql", "data/exchange_request.sql");
            }
        };
    }

    private void importSql(String... sqlFilePaths) {
        try {
            for (String path : sqlFilePaths)
                ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource(path));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateUsers(int number) {
        // Admin
        userRepository.save(User.builder()
                .email("admin@bee.com")
                .password(passwordEncoder.encode("admin"))
                .name("Admin")
                .gender(Gender.OTHER)
                .birthDate(LocalDate.now())
                .isVerified(true)
                .isLocked(false)
                .roles(Set.of(roleRepository.findByName("ADMIN").get()))
                .build());

        try {
            ResponseEntity<String> response = restTemplate.getForEntity("https://randomuser.me/api/?results=" + number, String.class);
            JsonNode root = new ObjectMapper().readTree(response.getBody());
            for (var node : root.get("results")) {
                String email = node.get("email").asText();
                String password = email.substring(0, email.indexOf("@"));
                String name = node.get("name").get("first").asText() + " " + node.get("name").get("last").asText();
                Gender gender = node.get("gender").asText().matches("male|female")
                        ? Gender.valueOf(node.get("gender").asText().toUpperCase())
                        : Gender.OTHER;
                LocalDate birthDate = LocalDate.parse(node.get("dob").get("date").asText().substring(0, 10));

                String phoneNumber = node.get("phone").asText().replaceAll("\\D", "");
                var communes = addressUnitRepository.findByTypeOrderByNameAsc(3);
                var commune = communes.get(new Random().nextInt(communes.size()));
                var district = addressUnitRepository.findById(commune.getParentId()).get();
                var province = addressUnitRepository.findById(district.getParentId()).get();
                String detailedAddress = node.get("location").get("street").get("number").asInt() + " " + node.get("location").get("street").get("name").asText();

                var contactInfo = ContactInfo.builder()
                        .phoneNumber(phoneNumber)
                        .province(province)
                        .district(district)
                        .commune(commune)
                        .detailedAddress(detailedAddress)
                        .build();
                contactInfo = contactInfoRepository.save(contactInfo);

                String pictureUrl = node.get("picture").get("large").asText();
                Set<Role> roles = Set.of(new Random().nextInt(10) == 0
                        ? roleRepository.findByName("BOOKSTORE").get()
                        : roleRepository.findByName("BOOK_EXCHANGER").get());

                User user = User.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .name(name)
                        .gender(gender)
                        .birthDate(birthDate)
                        .contactInfo(contactInfo)
                        .isVerified(true)
                        .isLocked(false)
                        .pictureUrl(pictureUrl)
                        .roles(roles)
                        .build();
                userRepository.save(user);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
