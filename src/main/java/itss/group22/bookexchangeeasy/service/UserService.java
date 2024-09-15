package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.user.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    AuthResponse authenticate(AuthRequest authRequest);
    void register(RegisterRequest registerRequest);
    void activate(ActivateRequest activateRequest);
    UserProfile getProfile(Long id);
    void updateProfile(Long id, UserProfile userProfile);
    void changePassword(Long id, ChangePasswordDTO changePasswordDTO);
    List<UserProfile> getUserList(int page, int size);
    List<UserProfile> searchUser(String keyword, int page, int size);
    void lockUserAccount(Long id);
    void unlockUserAccount(Long id);
    String uploadAvatar(Long id, MultipartFile image) throws IOException;
    void requestForgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
    ValidateKeyResponse validateKey(String key);
}
