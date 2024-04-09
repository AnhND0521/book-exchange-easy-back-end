package itss.group22.bookexchangeeasy.controller;

import itss.group22.bookexchangeeasy.dto.*;
import itss.group22.bookexchangeeasy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthRequest authRequest) {
        return ResponseEntity.ok(userService.authenticate(authRequest));
    }

    @PostMapping("/register")
    private ResponseEntity<ResponseMessage> register(@RequestBody @Valid RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.ok(new ResponseMessage("User account created successfully"));
    }

    @GetMapping("/{id}")
    private ResponseEntity<UserProfile> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @PutMapping("/{id}")

    private ResponseEntity<ResponseMessage> updateProfile(@PathVariable Long id, @RequestBody @Valid UserProfile userProfile) {
        userService.updateProfile(id, userProfile);
        return ResponseEntity.ok(new ResponseMessage("User profile updated successfully"));
    }

    @PutMapping("/{id}/change-password")
    private ResponseEntity<ResponseMessage> changePassword(@PathVariable Long id, @RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(id, changePasswordDTO);
        return ResponseEntity.ok(new ResponseMessage("Password changed successfully"));
    }
}
