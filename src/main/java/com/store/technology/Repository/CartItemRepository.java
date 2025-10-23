package com.store.technology.Repository;

import com.store.technology.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query(value = "SELECT * FROM cart_items", nativeQuery = true)
    List<CartItem> findAllIncludingDeleted();

    @Query(value = "SELECT * FROM cart_items WHERE id = ?1", nativeQuery = true)
    Optional<CartItem> findByIdIncludingDeleted(Long id);
}
