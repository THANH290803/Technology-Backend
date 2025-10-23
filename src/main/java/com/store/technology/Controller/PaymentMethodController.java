package com.store.technology.Controller;

import com.store.technology.Entity.PaymentMethod;
import com.store.technology.Service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping
    public List<PaymentMethod> getAll() {
        return paymentMethodService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<PaymentMethod> getById(@PathVariable Long id) {
        return paymentMethodService.getById(id);
    }

    @PostMapping
    public PaymentMethod create(@RequestBody PaymentMethod paymentMethod) {
        return paymentMethodService.create(paymentMethod);
    }

    @PutMapping("/{id}")
    public PaymentMethod update(@PathVariable Long id, @RequestBody PaymentMethod paymentMethod) {
        return paymentMethodService.update(id, paymentMethod);
    }

    @DeleteMapping("/{id}")
    public boolean softDelete(@PathVariable Long id) {
        return paymentMethodService.softDelete(id);
    }

    @PutMapping("/restore/{id}")
    public boolean restore(@PathVariable Long id) {
        return paymentMethodService.restore(id);
    }
}
