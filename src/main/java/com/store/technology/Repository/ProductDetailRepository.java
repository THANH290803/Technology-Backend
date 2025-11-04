package com.store.technology.Repository;

import com.store.technology.Entity.ProductDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    @Query("SELECT pd FROM ProductDetail pd WHERE pd.deletedAt IS NULL")
    List<ProductDetail> findAllNotDeleted();

    @Query("SELECT pd FROM ProductDetail pd")
    List<ProductDetail> findAllIncludingDeleted();

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.id = :id AND pd.deletedAt IS NULL")
    ProductDetail findNotDeletedById(@Param("id") Long id);

    @Query("SELECT pd FROM ProductDetail pd WHERE pd.id = :id")
    ProductDetail findAnyById(@Param("id") Long id);

    // Phải trả về Optional
    Optional<ProductDetail> findByProduct_IdAndConfiguration_Id(Long productId, Long configurationId);

    void deleteAllByProductId(Long productId);

    // 🆕 Thêm hàm này để lấy danh sách ProductDetail theo productId
    @Query("SELECT pd FROM ProductDetail pd WHERE pd.product.id = :productId AND pd.deletedAt IS NULL")
    List<ProductDetail> findByProductId(Long productId);

}
