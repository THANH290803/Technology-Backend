package com.store.technology.Controller;

import com.store.technology.Entity.OrderDetail;
import com.store.technology.Service.OrderDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order Details", description = "CRUD API cho Order Details (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/order-details")
@CrossOrigin("*")
public class OrderDetailController {
    private final OrderDetailService service;

    public OrderDetailController(OrderDetailService service) {
        this.service = service;
    }

    // ✅ 1. Lấy các đơn hàng chi tiết chưa xoá mềm
    @GetMapping
    @Operation(
            summary = "Lấy danh sách chi tiết đơn hàng đang hoạt động",
            description = "Trả về tất cả các bản ghi Order Detail chưa bị xóa mềm."
    )
    public ResponseEntity<List<OrderDetail>> getAllNotDeleted() {
        return ResponseEntity.ok(service.getAllNotDeleted());
    }

    // ✅ 2. Lấy tất cả các chi tiết đơn hàng (kể cả xoá)
    @GetMapping("/all")
    @Operation(
            summary = "Lấy toàn bộ chi tiết đơn hàng (bao gồm cả đã xóa)",
            description = "Trả về danh sách tất cả các bản ghi Order Detail, kể cả những bản đã bị xóa mềm."
    )
    public ResponseEntity<List<OrderDetail>> getAllIncludingDeleted() {
        return ResponseEntity.ok(service.getAllIncludingDeleted());
    }

    // ✅ 3. Lấy 1 đơn hàng chi tiết chưa xoá mềm
    @GetMapping("/{id}")
    @Operation(
            summary = "Lấy chi tiết đơn hàng theo ID (chưa bị xóa)",
            description = "Trả về thông tin chi tiết của một Order Detail theo ID, chỉ khi bản ghi chưa bị xóa mềm."
    )
    public ResponseEntity<OrderDetail> getNotDeletedById(@PathVariable Long id) {
        OrderDetail detail = service.getNotDeletedById(id);
        return detail != null ? ResponseEntity.ok(detail) : ResponseEntity.notFound().build();
    }

    // ✅ 4. Lấy 1 đơn hàng chi tiết (kể cả xoá mềm)
    @GetMapping("/{id}/any")
    @Operation(
            summary = "Lấy chi tiết đơn hàng theo ID (kể cả đã xóa)",
            description = "Trả về thông tin chi tiết của một Order Detail theo ID, kể cả bản ghi đã bị xóa mềm."
    )
    public ResponseEntity<OrderDetail> getAnyById(@PathVariable Long id) {
        OrderDetail detail = service.getAnyById(id);
        return detail != null ? ResponseEntity.ok(detail) : ResponseEntity.notFound().build();
    }

    // ✅ 5. Thêm mới
    @PatchMapping("/create")
    @Operation(
            summary = "Tạo chi tiết đơn hàng mới",
            description = "Thêm mới một bản ghi chi tiết đơn hàng (Order Detail) vào cơ sở dữ liệu."
    )
    public ResponseEntity<OrderDetail> create(@RequestBody OrderDetail detail) {
        return ResponseEntity.ok(service.create(detail));
    }

    // ✅ 6. Cập nhật (PATCH)
    @PatchMapping("/{id}/update")
    @Operation(
            summary = "Cập nhật chi tiết đơn hàng",
            description = "Cập nhật thông tin của một bản ghi Order Detail theo ID (sử dụng PATCH)."
    )
    public ResponseEntity<OrderDetail> update(@PathVariable Long id, @RequestBody OrderDetail detail) {
        OrderDetail updated = service.update(id, detail);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // ✅ 7. Xoá mềm
    @PatchMapping("/{id}/delete")
    @Operation(
            summary = "Xóa mềm chi tiết đơn hàng",
            description = "Đánh dấu một bản ghi Order Detail là đã bị xóa mềm mà không xóa khỏi cơ sở dữ liệu."
    )
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ 8. Khôi phục
    @PatchMapping("/{id}/restore")
    @Operation(
            summary = "Khôi phục chi tiết đơn hàng đã xóa",
            description = "Khôi phục lại một bản ghi Order Detail đã bị xóa mềm."
    )
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        service.restore(id);
        return ResponseEntity.noContent().build();
    }
}
