package com.store.technology.Repository;

import com.store.technology.Entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    // Lấy tất cả (kể cả brand đã xoá)
    @Query(value = "SELECT * FROM brands", nativeQuery = true)
    List<Brand> findAllIncludingDeleted();

    // Tìm theo id (kể cả đã xoá)
    @Query(value = "SELECT * FROM brands WHERE id = :id", nativeQuery = true)
    Optional<Brand> findByIdIncludingDeleted(Long id);
}
