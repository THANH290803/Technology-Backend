package com.store.technology.Repository;

import com.store.technology.Entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    // Lấy tất cả chưa bị xoá
    List<Configuration> findAllByDeletedAtIsNull();

    // Lấy tất cả đã bị xoá
    List<Configuration> findAllByDeletedAtIsNotNull();

    // Lấy 1 bản ghi chưa xoá
    Optional<Configuration> findByIdAndDeletedAtIsNull(Long id);
}
