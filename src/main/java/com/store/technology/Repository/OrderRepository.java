package com.store.technology.Repository;

import com.store.technology.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // ✅ Lấy tất cả (kể cả đã xoá)
    @Query(value = "SELECT * FROM orders", nativeQuery = true)
    List<Order> findAllIncludingDeleted();

    // ✅ Lấy tất cả chưa xoá
    @Query(value = "SELECT * FROM orders WHERE deleted_at IS NULL", nativeQuery = true)
    List<Order> findAllNotDeleted();

    // ✅ Lấy tất cả đã xoá
    @Query(value = "SELECT * FROM orders WHERE deleted_at IS NOT NULL", nativeQuery = true)
    List<Order> findAllDeleted();

    // ✅ Lấy 1 (kể cả đã xoá)
    @Query(value = "SELECT * FROM orders WHERE id = :id", nativeQuery = true)
    Order findAnyById(@Param("id") Long id);

    // ✅ Lấy 1 (chưa xoá)
    @Query(value = "SELECT * FROM orders WHERE id = :id AND deleted_at IS NULL", nativeQuery = true)
    Order findNotDeletedById(@Param("id") Long id);

    List<Order> findAllByDeletedAtIsNull();
}
