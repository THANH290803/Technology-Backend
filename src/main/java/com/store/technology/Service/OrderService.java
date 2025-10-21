package com.store.technology.Service;

import com.store.technology.Entity.Order;
import com.store.technology.Repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders(boolean includeDeleted) {
        return includeDeleted ? orderRepository.findAllIncludingDeleted() : orderRepository.findAllNotDeleted();
    }

    public List<Order> getDeletedOrders() {
        return orderRepository.findAllDeleted();
    }

    public Optional<Order> getOrderById(Long id, boolean includeDeleted) {
        if (includeDeleted) {
            return Optional.ofNullable(orderRepository.findAnyById(id));
        } else {
            return Optional.ofNullable(orderRepository.findNotDeletedById(id));
        }
    }

    public Order createOrder(Order order) {
        order.setId(null);
        return orderRepository.save(order);
    }

    public Order patchUpdateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findAnyById(id);
        if (order == null) return null;

        if (orderDetails.getStatus() != null) order.setStatus(orderDetails.getStatus());
        if (orderDetails.getCustomerName() != null) order.setCustomerName(orderDetails.getCustomerName());
        if (orderDetails.getCustomerPhone() != null) order.setCustomerPhone(orderDetails.getCustomerPhone());
        if (orderDetails.getCustomerAddress() != null) order.setCustomerAddress(orderDetails.getCustomerAddress());
        if (orderDetails.getPaymentMethod() != null) order.setPaymentMethod(orderDetails.getPaymentMethod());
        if (orderDetails.getUserId() != null) order.setUserId(orderDetails.getUserId());

        return orderRepository.save(order);
    }

    public boolean softDeleteOrder(Long id) {
        Order order = orderRepository.findNotDeletedById(id);
        if (order == null) return false;
        order.setDeletedAt(LocalDateTime.now());
        orderRepository.save(order);
        return true;
    }

    public boolean restoreOrder(Long id) {
        Order order = orderRepository.findAnyById(id);
        if (order == null || order.getDeletedAt() == null) return false;
        order.setDeletedAt(null);
        orderRepository.save(order);
        return true;
    }
}

