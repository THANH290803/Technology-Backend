package com.store.technology.Controller;

import com.store.technology.Entity.User;
import com.store.technology.Security.JwtTokenUtil;
import com.store.technology.Service.RoleService;
import com.store.technology.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "User", description = "CRUD API cho User (Soft Delete + Restore + Login)")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    // 1. Lấy tất cả user chưa xoá
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // 2. Lấy tất cả user kể cả đã xoá
    @GetMapping("/all")
    public List<User> getAllUsersIncludingDeleted() {
        return userService.getAllUsersIncludingDeleted();
    }

    // 3. Lấy 1 user theo id (chưa xoá)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Tạo user mới
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());

        User createdUser = userService.createUser(user, request.getRoleId());
        return ResponseEntity.ok(createdUser);
    }

    // 5. Update user
    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@PathVariable Long id, @RequestBody PatchUserRequest request) {
        User updatedUser = userService.patchUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    // 6. Soft delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteUser(@PathVariable Long id) {
        userService.softDeleteUser(id);
        return ResponseEntity.ok("User soft deleted successfully");
    }

    // 7. Restore user
    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restoreUser(@PathVariable Long id) {
        userService.restoreUser(id);
        return ResponseEntity.ok("User restored successfully");
    }

    @Data
    public static class CreateUserRequest {
        private String username;
        private String email;
        private String password;
        private String phoneNumber;
        private String address;
        private Long roleId;
    }

    @Data
    public static class PatchUserRequest {
        private String username;
        private String email;
        private String password;
        private String phoneNumber;
        private String address;
        private Long roleId;
    }
}
