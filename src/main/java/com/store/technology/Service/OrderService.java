package com.store.technology.Service;

import com.store.technology.Entity.Order;
import com.store.technology.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Lấy tất cả đơn hàng chưa bị xóa (lọc trong Java)
    public List<Order> getAll() {
        return orderRepository.findAllIncludingDeleted()
                .stream()
                .filter(order -> order.getDeletedAt() == null)
                .collect(Collectors.toList());
    }

    //  Lấy 1 đơn hàng chưa bị xóa
    public Optional<Order> getById(Long id) {
        return orderRepository.findByIdIncludingDeleted(id)
                .filter(order -> order.getDeletedAt() == null);
    }

    //  Tạo mới
    public Order create(Order order) {
        order.setCreatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    // Cập nhật
    public Order update(Long id, Order updated) {
        Optional<Order> orderOpt = getById(id);
        if (orderOpt.isEmpty()) return null;

        Order order = orderOpt.get();
        order.setStatus(updated.getStatus());
        order.setTotalPrice(updated.getTotalPrice());
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    // Xóa mềm
    public boolean softDelete(Long id) {
        Optional<Order> orderOpt = getById(id);
        if (orderOpt.isEmpty()) return false;

        Order order = orderOpt.get();
        order.setDeletedAt(LocalDateTime.now());
        orderRepository.save(order);
        return true;
    }

    // Khôi phục đơn hàng
    public boolean restore(Long id) {
        Optional<Order> orderOpt = orderRepository.findByIdIncludingDeleted(id);
        if (orderOpt.isEmpty()) return false;

        Order order = orderOpt.get();
        order.setDeletedAt(null);
        orderRepository.save(order);
        return true;
    }
}
