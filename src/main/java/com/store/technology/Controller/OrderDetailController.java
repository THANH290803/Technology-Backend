package com.store.technology.Controller;

import com.store.technology.Entity.OrderDetail;
import com.store.technology.Service.OrderDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order Details", description = "CRUD API cho Order Details (Soft Delete + Restore)")
@RestController
@RequestMapping("/order-details")
@CrossOrigin("*")
public class OrderDetailController {
    private final OrderDetailService service;

    public OrderDetailController(OrderDetailService service) {
        this.service = service;
    }

    // ✅ 1. Lấy các đơn hàng chi tiết chưa xoá mềm
    @GetMapping
    public ResponseEntity<List<OrderDetail>> getAllNotDeleted() {
        return ResponseEntity.ok(service.getAllNotDeleted());
    }

    // ✅ 2. Lấy tất cả các chi tiết đơn hàng (kể cả xoá)
    @GetMapping("/all")
    public ResponseEntity<List<OrderDetail>> getAllIncludingDeleted() {
        return ResponseEntity.ok(service.getAllIncludingDeleted());
    }

    // ✅ 3. Lấy 1 đơn hàng chi tiết chưa xoá mềm
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetail> getNotDeletedById(@PathVariable Long id) {
        OrderDetail detail = service.getNotDeletedById(id);
        return detail != null ? ResponseEntity.ok(detail) : ResponseEntity.notFound().build();
    }

    // ✅ 4. Lấy 1 đơn hàng chi tiết (kể cả xoá mềm)
    @GetMapping("/{id}/any")
    public ResponseEntity<OrderDetail> getAnyById(@PathVariable Long id) {
        OrderDetail detail = service.getAnyById(id);
        return detail != null ? ResponseEntity.ok(detail) : ResponseEntity.notFound().build();
    }

    // ✅ 5. Thêm mới
    @PatchMapping("/create")
    public ResponseEntity<OrderDetail> create(@RequestBody OrderDetail detail) {
        return ResponseEntity.ok(service.create(detail));
    }

    // ✅ 6. Cập nhật (PATCH)
    @PatchMapping("/{id}/update")
    public ResponseEntity<OrderDetail> update(@PathVariable Long id, @RequestBody OrderDetail detail) {
        OrderDetail updated = service.update(id, detail);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // ✅ 7. Xoá mềm
    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ 8. Khôi phục
    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        service.restore(id);
        return ResponseEntity.noContent().build();
    }
}
