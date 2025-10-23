package com.store.technology.Controller;

import com.store.technology.Entity.Order;
import com.store.technology.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<Order> getById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.create(order);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable Long id, @RequestBody Order order) {
        return orderService.update(id, order);
    }

    @DeleteMapping("/{id}")
    public boolean softDelete(@PathVariable Long id) {
        return orderService.softDelete(id);
    }

    @PutMapping("/restore/{id}")
    public boolean restore(@PathVariable Long id) {
        return orderService.restore(id);
    }
}
