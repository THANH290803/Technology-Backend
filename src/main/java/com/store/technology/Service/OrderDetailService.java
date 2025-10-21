package com.store.technology.Service;

import com.store.technology.Entity.OrderDetail;
import com.store.technology.Repository.OrderDetailRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderDetailService {
    private final OrderDetailRepository repository;

    public OrderDetailService(OrderDetailRepository repository) {
        this.repository = repository;
    }

    public List<OrderDetail> getAllNotDeleted() {
        return repository.findAllNotDeleted();
    }

    public List<OrderDetail> getAllIncludingDeleted() {
        return repository.findAllIncludingDeleted();
    }

    public OrderDetail getNotDeletedById(Long id) {
        return repository.findNotDeletedById(id);
    }

    public OrderDetail getAnyById(Long id) {
        return repository.findAnyById(id);
    }

    public OrderDetail create(OrderDetail detail) {
        return repository.save(detail);
    }

    public OrderDetail update(Long id, OrderDetail updated) {
        OrderDetail existing = repository.findAnyById(id);
        if (existing == null) return null;

        existing.setQuantity(updated.getQuantity());
        existing.setUnitPrice(updated.getUnitPrice());
        existing.setOrder(updated.getOrder());
        existing.setProductDetail(updated.getProductDetail());
        return repository.save(existing);
    }

    public void softDelete(Long id) {
        OrderDetail existing = repository.findNotDeletedById(id);
        if (existing != null) {
            existing.setDeletedAt(LocalDateTime.now());
            repository.save(existing);
        }
    }

    public void restore(Long id) {
        OrderDetail existing = repository.findAnyById(id);
        if (existing != null && existing.getDeletedAt() != null) {
            existing.setDeletedAt(null);
            repository.save(existing);
        }
    }
}
