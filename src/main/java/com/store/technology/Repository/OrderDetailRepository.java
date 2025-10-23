package com.store.technology.Repository;

import com.store.technology.Entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query(value = "SELECT * FROM order_details", nativeQuery = true)
    List<OrderDetail> findAllIncludingDeleted();

    @Query(value = "SELECT * FROM order_details WHERE id = ?1", nativeQuery = true)
    Optional<OrderDetail> findByIdIncludingDeleted(Long id);
}
