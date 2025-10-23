package com.store.technology.Service;

import com.store.technology.Entity.OrderDetail;
import com.store.technology.Repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    // Lấy tất cả (Hibernate @Where tự lọc deleted_at)
    public List<OrderDetail> getAll() {
        return orderDetailRepository.findAll();
    }

    // Lấy chi tiết theo ID
    public Optional<OrderDetail> getById(Long id) {
        return orderDetailRepository.findById(id);
    }

    // Tạo mới
    public OrderDetail create(OrderDetail detail) {
        return orderDetailRepository.save(detail);
    }

    // Cập nhật
    public OrderDetail update(Long id, OrderDetail data) {
        Optional<OrderDetail> opt = orderDetailRepository.findById(id);
        if (opt.isEmpty()) return null;

        OrderDetail detail = opt.get();
        if (data.getQuantity() != null) detail.setQuantity(data.getQuantity());
        if (data.getPrice() != null) detail.setPrice(data.getPrice());
        if (data.getOrder() != null) detail.setOrder(data.getOrder());
        if (data.getProduct() != null) detail.setProduct(data.getProduct());

        return orderDetailRepository.save(detail);
    }

    // Soft delete (đánh dấu deleted_at)
    public boolean softDelete(Long id) {
        Optional<OrderDetail> opt = orderDetailRepository.findById(id);
        if (opt.isEmpty()) return false;

        OrderDetail detail = opt.get();
        detail.setDeletedAt(LocalDateTime.now());
        orderDetailRepository.save(detail);
        return true;
    }

    // Restore (xóa mốc deleted_at)
    public boolean restore(Long id) {
        Optional<OrderDetail> opt = orderDetailRepository.findByIdIncludingDeleted(id);
        if (opt.isEmpty()) return false;

        OrderDetail detail = opt.get();
        detail.setDeletedAt(null);
        orderDetailRepository.save(detail);
        return true;
    }
}
