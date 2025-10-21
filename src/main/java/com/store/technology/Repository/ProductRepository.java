package com.store.technology.Repository;

import com.store.technology.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByIsDeleted(boolean isDeleted);

    Optional<Product> findByIdAndIsDeleted(Long id, boolean isDeleted);

}
