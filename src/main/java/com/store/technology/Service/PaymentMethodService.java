package com.store.technology.Service;

import com.store.technology.Entity.PaymentMethod;
import com.store.technology.Repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    // Lấy tất cả (lọc trong Java)
    public List<PaymentMethod> getAll() {
        return paymentMethodRepository.findAllIncludingDeleted()
                .stream()
                .filter(method -> method.getDeletedAt() == null)
                .collect(Collectors.toList());
    }

    // Lấy 1 bản ghi chưa bị xóa (lọc trong Java)
    public Optional<PaymentMethod> getById(Long id) {
        return paymentMethodRepository.findByIdIncludingDeleted(id)
                .filter(method -> method.getDeletedAt() == null);
    }

    // Tạo mới
    public PaymentMethod create(PaymentMethod method) {
        method.setCreatedAt(LocalDateTime.now());
        return paymentMethodRepository.save(method);
    }

    // Cập nhật
    public PaymentMethod update(Long id, PaymentMethod data) {
        Optional<PaymentMethod> opt = getById(id);
        if (opt.isEmpty()) return null;

        PaymentMethod method = opt.get();
        method.setName(data.getName());
        method.setDescription(data.getDescription());
        method.setUpdatedAt(LocalDateTime.now());
        return paymentMethodRepository.save(method);
    }

    // Xóa mềm
    public boolean softDelete(Long id) {
        Optional<PaymentMethod> opt = getById(id);
        if (opt.isEmpty()) return false;

        PaymentMethod method = opt.get();
        method.setDeletedAt(LocalDateTime.now());
        paymentMethodRepository.save(method);
        return true;
    }

    // Khôi phục
    public boolean restore(Long id) {
        Optional<PaymentMethod> opt = paymentMethodRepository.findByIdIncludingDeleted(id);
        if (opt.isEmpty()) return false;

        PaymentMethod method = opt.get();
        method.setDeletedAt(null);
        paymentMethodRepository.save(method);
        return true;
    }
}
