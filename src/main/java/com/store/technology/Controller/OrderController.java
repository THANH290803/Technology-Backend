package com.store.technology.Controller;

import com.store.technology.Entity.Order;
import com.store.technology.Service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders", description = "CRUD API cho Orders (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ✅ API riêng: lấy các đơn hàng chưa bị xoá mềm
    @GetMapping
    public ResponseEntity<List<Order>> getActiveOrders() {
        return ResponseEntity.ok(orderService.getAllOrders(false));
    }

    // ✅ Lấy tất cả đơn hàng (có thể bao gồm đã xoá nếu includeDeleted=true)
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam(defaultValue = "false") boolean includeDeleted) {
        return ResponseEntity.ok(orderService.getAllOrders(includeDeleted));
    }

    // ✅ Lấy 1 đơn hàng (chưa xoá hoặc bao gồm xoá nếu includeDeleted=true)
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id,
                                              @RequestParam(defaultValue = "false") boolean includeDeleted) {
        return orderService.getOrderById(id, includeDeleted)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Tạo đơn hàng
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    // ✅ Cập nhật đơn hàng (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<Order> patchUpdateOrder(@PathVariable Long id, @RequestBody Order order) {
        Order updated = orderService.patchUpdateOrder(id, order);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // ✅ Xoá mềm đơn hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteOrder(@PathVariable Long id) {
        boolean deleted = orderService.softDeleteOrder(id);
        return deleted ? ResponseEntity.ok("Order soft-deleted successfully") : ResponseEntity.notFound().build();
    }

    // ✅ Khôi phục đơn hàng đã xoá
    @PatchMapping("/{id}/restore")
    public ResponseEntity<String> restoreOrder(@PathVariable Long id) {
        boolean restored = orderService.restoreOrder(id);
        return restored ? ResponseEntity.ok("Order restored successfully") : ResponseEntity.notFound().build();
    }
}
