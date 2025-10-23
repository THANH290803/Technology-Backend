package com.store.technology.Repository;

import com.store.technology.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Lấy tất cả sản phẩm chưa bị xoá
    @Query(value = "SELECT * FROM products WHERE deleted_at IS NULL", nativeQuery = true)
    List<Product> findAllActive();

    // Lấy tất cả sản phẩm kể cả đã xoá
    @Query(value = "SELECT * FROM products", nativeQuery = true)
    List<Product> findAllIncludingDeleted();

    // Tìm theo id (chưa bị xoá)
    @Query(value = "SELECT * FROM products WHERE id = :id AND deleted_at IS NULL", nativeQuery = true)
    Optional<Product> findActiveById(@Param("id") Long id);

    // Tìm theo id (kể cả đã xoá)
    @Query(value = "SELECT * FROM products WHERE id = :id", nativeQuery = true)
    Optional<Product> findByIdIncludingDeleted(@Param("id") Long id);

    // Tìm sản phẩm theo tên (chưa bị xoá)
    @Query(value = "SELECT * FROM products WHERE name LIKE %:keyword% AND deleted_at IS NULL", nativeQuery = true)
    List<Product> searchActiveByName(@Param("keyword") String keyword);
}
