package com.store.technology.Repository;

import com.store.technology.Entity.OrderDetail;
import com.store.technology.Entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    // ✅ Lấy tất cả chi tiết đơn hàng chưa bị xoá mềm
    @Query("SELECT OrderDetail FROM OrderDetail WHERE deletedAt IS NULL")
    List<OrderDetail> findAllNotDeleted();

    // ✅ Lấy tất cả chi tiết đơn hàng (kể cả đã xoá)
    @Query("SELECT OrderDetail FROM OrderDetail")
    List<OrderDetail> findAllIncludingDeleted();

    // ✅ Lấy 1 chi tiết đơn hàng chưa bị xoá mềm
    @Query("SELECT OrderDetail FROM OrderDetail WHERE id = :id AND deletedAt IS NULL")
    OrderDetail findNotDeletedById(Long id);

    // ✅ Lấy 1 chi tiết đơn hàng kể cả đã xoá mềm
    @Query("SELECT OrderDetail FROM OrderDetail WHERE id = :id")
    OrderDetail findAnyById(Long id);

    // Truy vấn thông qua ProductDetail quan hệ ManyToOne
    Optional<OrderDetail> findByProductDetail_Product_IdAndProductDetail_Configuration_Id(Long productId, Long configurationId);

}
