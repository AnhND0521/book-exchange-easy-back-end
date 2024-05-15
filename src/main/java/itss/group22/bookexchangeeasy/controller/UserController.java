package itss.group22.bookexchangeeasy.controller;

import io.swagger.v3.oas.annotations.Operation;
import itss.group22.bookexchangeeasy.dto.*;
import itss.group22.bookexchangeeasy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth")
    @Operation(
            summary = "Xác thực người dùng",
            description = "Xác thực qua email và password, trả về access token"
    )
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthRequest authRequest) {
        return ResponseEntity.ok(userService.authenticate(authRequest));
    }

    @PostMapping("/register")
    @Operation(
            summary = "Đăng ký người dùng mới",
            description = "Đăng ký với request body như bên dưới. " +
                    "Chỉ có 6 trường đầu là bắt buộc. " +
                    "Trường roles nhận mảng gồm giá trị 'BOOK_EXCHANGER' hoặc 'BOOKSTORE'. " +
                    "Trường gender nhận một trong các giá trị 'MALE', 'FEMALE' hoặc 'OTHER'. " +
                    "Với các trường provinceId, districtId và communeId, trước tiên lấy data từ các API trong phần address-controller sau đó cho người dùng chọn rồi lấy id truyền vào."
    )
    private ResponseEntity<ResponseMessage> register(@RequestBody @Valid RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.ok(new ResponseMessage("User account created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy profile người dùng")
    private ResponseEntity<UserProfile> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Cập nhật profile người dùng",
            description = "Body tương tự như đăng ký. " +
                    "Không cần điền trường email do email là cố định không thể cập nhật. " +
                    "Các trường province, district và commune chỉ cần thuộc tính id."
    )
    private ResponseEntity<ResponseMessage> updateProfile(@PathVariable Long id, @RequestBody @Valid UserProfile userProfile) {
        userService.updateProfile(id, userProfile);
        return ResponseEntity.ok(new ResponseMessage("User profile updated successfully"));
    }

    @PutMapping("/{id}/change-password")
    @Operation(summary = "Thay đổi mật khẩu")
    private ResponseEntity<ResponseMessage> changePassword(@PathVariable Long id, @RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(id, changePasswordDTO);
        return ResponseEntity.ok(new ResponseMessage("Password changed successfully"));
    }

    @GetMapping("")
    @Operation(summary = "Lấy danh sách tất cả người dùng")
    private ResponseEntity<List<UserProfile>> getUserList(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size
    ) {
        return ResponseEntity.ok(userService.getUserList(page, size));
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm người dùng")
    private ResponseEntity<List<UserProfile>> searchUser(
            @RequestParam(name = "q") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size
    ) {
        return ResponseEntity.ok(userService.searchUser(keyword, page, size));
    }

    @PutMapping("/{id}/lock")
    @Operation(summary = "Khoá tài khoản người dùng")
    private ResponseEntity<ResponseMessage> lockUserAccount(@PathVariable Long id) {
        userService.lockUserAccount(id);
        return ResponseEntity.ok(new ResponseMessage("User account locked successfully"));
    }

    @PutMapping("/{id}/unlock")
    @Operation(summary = "Mở khoá tài khoản người dùng")
    private ResponseEntity<ResponseMessage> unlockUserAccount(@PathVariable Long id) {
        userService.unlockUserAccount(id);
        return ResponseEntity.ok(new ResponseMessage("User account unlocked successfully"));
    }
}
