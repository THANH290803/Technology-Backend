package com.store.technology.Repository;

import com.store.technology.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    // Lấy tất cả user kể cả đã xoá
    @Query(value = "SELECT * FROM users", nativeQuery = true)
    List<User> findAllIncludingDeleted();

    // Lấy user theo id kể cả đã xoá
    @Query(value = "SELECT * FROM users WHERE id = ?1", nativeQuery = true)
    Optional<User> findByIdIncludingDeleted(Long id);

    Optional<User> findByUsernameAndDeletedAtIsNull(String username);
}
