package com.store.technology.Repository;

import com.store.technology.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Lấy tất cả category chưa bị xoá (deleted_at IS NULL)
    @Query(value = "SELECT * FROM categories WHERE deleted_at IS NULL", nativeQuery = true)
    List<Category> findAllActive();

    // Lấy tất cả category kể cả đã xoá
    @Query(value = "SELECT * FROM categories", nativeQuery = true)
    List<Category> findAllIncludingDeleted();

    // Tìm 1 category theo id (chỉ lấy cái chưa bị xoá)
    @Query(value = "SELECT * FROM categories WHERE id = :id AND deleted_at IS NULL", nativeQuery = true)
    Optional<Category> findActiveById(@Param("id") Long id);

    // Tìm 1 category theo id (kể cả đã xoá)
    @Query(value = "SELECT * FROM categories WHERE id = :id", nativeQuery = true)
    Optional<Category> findByIdIncludingDeleted(@Param("id") Long id);
}
