package com.store.technology.Authorize;

import com.store.technology.Entity.User;
import com.store.technology.Repository.RoleRepository;
import com.store.technology.Repository.UserRepository;
import com.store.technology.Security.JwtTokenUtil;
import com.store.technology.Service.RoleService;
import com.store.technology.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;

    // Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());

        User savedUser = userService.createUser(user, request.getRoleId());
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate by email
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userService.findByEmail(request.getEmail());

            String token = jwtTokenUtil.generateToken(user.getEmail()); // JWT cũng theo email

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "phoneNumber", user.getPhoneNumber(),
                    "address", user.getAddress(),
                    "role", user.getRole().getName()
            ));

            return ResponseEntity.ok(data);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }
    }

    // DTO để trả về user an toàn
//    @Data
//    @AllArgsConstructor
//    static class JwtLoginResponse {
//        private String token;
//        private UserDTO user;
//    }

    @Data
    static class UserDTO {
        private Long id;
        private String username;
        private String email;
        private String phoneNumber;
        private String address;
        private String role;

//        public UserDTO(User user) {
//            this.id = user.getId();
//            this.username = user.getUsername();
//            this.email = user.getEmail();
//            this.phoneNumber = user.getPhoneNumber();
//            this.address = user.getAddress();
//            this.role = user.getRole().getName(); // assuming role entity has getName()
//        }
    }

    @Data
    static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
        private String phoneNumber;
        private String address;
        private Long roleId;
    }
}