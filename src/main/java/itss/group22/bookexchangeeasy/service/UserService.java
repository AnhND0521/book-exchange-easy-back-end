package itss.group22.bookexchangeeasy.service;

import itss.group22.bookexchangeeasy.dto.AuthRequest;
import itss.group22.bookexchangeeasy.dto.AuthResponse;
import itss.group22.bookexchangeeasy.dto.RegisterRequest;

public interface UserService {
    AuthResponse authenticate(AuthRequest authRequest);
    void register(RegisterRequest registerRequest);
}
