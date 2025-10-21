package com.store.technology.Repository;

import com.store.technology.Entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    // ✅ Lấy tất cả (kể cả đã xoá)
    @Query("SELECT p FROM ProductDetail p")
    List<ProductDetail> findAllIncludingDeleted();

    // ✅ Lấy tất cả chưa xoá
    @Query("SELECT p FROM ProductDetail p WHERE p.deletedAt IS NULL")
    List<ProductDetail> findAllNotDeleted();

    // ✅ Lấy 1 (kể cả đã xoá)
    @Query("SELECT p FROM ProductDetail p WHERE p.id = :id")
    ProductDetail findAnyById(@Param("id") Long id);

    // ✅ Lấy 1 (chưa xoá)
    @Query("SELECT p FROM ProductDetail p WHERE p.id = :id AND p.deletedAt IS NULL")
    ProductDetail findNotDeletedById(@Param("id") Long id);

}
