package com.store.technology.Repository;

import com.store.technology.Entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    @Query(value = "SELECT * FROM product_details", nativeQuery = true)
    List<ProductDetail> findAllIncludingDeleted();

    @Query(value = "SELECT * FROM product_details WHERE id = ?1", nativeQuery = true)
    Optional<ProductDetail> findByIdIncludingDeleted(Long id);
}
