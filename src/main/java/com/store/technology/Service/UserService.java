package com.store.technology.Service;

import com.store.technology.Controller.UserController;
import com.store.technology.Entity.Role;
import com.store.technology.Entity.User;
import com.store.technology.Repository.RoleRepository;
import com.store.technology.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    // Lấy tất cả user chưa xoá
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Lấy tất cả user kể cả đã xoá
    public List<User> getAllUsersIncludingDeleted() {
        return userRepository.findAllIncludingDeleted();
    }

    // Lấy 1 user theo id (chưa xoá)
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Lấy 1 user theo id kể cả đã xoá
    public Optional<User> getUserByIdIncludingDeleted(Long id) {
        return userRepository.findByIdIncludingDeleted(id);
    }

    // Tạo user mới
    public User createUser(User user, Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Update user
    public User patchUser(Long id, UserController.PatchUserRequest request) {
        return userRepository.findById(id).map(user -> {
            if (request.getUsername() != null) user.setUsername(request.getUsername());
            if (request.getEmail() != null) user.setEmail(request.getEmail());
            if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
            if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
            if (request.getAddress() != null) user.setAddress(request.getAddress());
            if (request.getRoleId() != null) {
                roleRepository.findById(request.getRoleId()).ifPresent(user::setRole);
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }


    // Soft delete user
    public void softDeleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Restore user
    public void restoreUser(Long id) {
        userRepository.findByIdIncludingDeleted(id).ifPresent(user -> {
            user.setDeletedAt(null);
            userRepository.save(user);
        });
    }


    // Phương thức tìm user theo username
    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
