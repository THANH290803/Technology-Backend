package com.store.technology.Repository;

import com.store.technology.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT * FROM carts", nativeQuery = true)
    List<Cart> findAllIncludingDeleted();

    @Query(value = "SELECT * FROM carts WHERE id = ?1", nativeQuery = true)
    Optional<Cart> findByIdIncludingDeleted(Long id);
}
