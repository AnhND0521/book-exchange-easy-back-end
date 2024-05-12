package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.*;

import java.util.List;

public interface UserService {
    AuthResponse authenticate(AuthRequest authRequest);
    void register(RegisterRequest registerRequest);
    UserProfile getProfile(Long id);
    void updateProfile(Long id, UserProfile userProfile);
    void changePassword(Long id, ChangePasswordDTO changePasswordDTO);
    List<UserProfile> getUserList(int page, int size);
    List<UserProfile> searchUser(String keyword, int page, int size);
}
