package com.store.technology.Repository;

import com.store.technology.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM orders", nativeQuery = true)
    List<Order> findAllIncludingDeleted();

    @Query(value = "SELECT * FROM orders WHERE id = ?1", nativeQuery = true)
    Optional<Order> findByIdIncludingDeleted(Long id);
}
