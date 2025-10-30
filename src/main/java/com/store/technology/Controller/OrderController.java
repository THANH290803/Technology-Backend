package com.store.technology.Controller;

import com.store.technology.DTO.OrderRequest;
import com.store.technology.Entity.Order;
import com.store.technology.Service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "Lấy danh sách đơn hàng đang hoạt động",
            description = "Trả về tất cả đơn hàng chưa bị xóa mềm."
    )
    public ResponseEntity<List<Order>> getActiveOrders() {
        return ResponseEntity.ok(orderService.getAllOrders(false));
    }

    // ✅ Lấy tất cả đơn hàng (có thể bao gồm đã xoá nếu includeDeleted=true)
    @GetMapping("/all")
    @Operation(
            summary = "Lấy tất cả đơn hàng (bao gồm cả đã xóa)",
            description = "Trả về danh sách toàn bộ đơn hàng. Sử dụng query param `includeDeleted=true` để lấy cả đơn hàng đã bị xóa mềm."
    )
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam(defaultValue = "false") boolean includeDeleted) {
        return ResponseEntity.ok(orderService.getAllOrders(includeDeleted));
    }

    // ✅ Lấy 1 đơn hàng (chưa xoá hoặc bao gồm xoá nếu includeDeleted=true)
    @GetMapping("/{id}")
    @Operation(
            summary = "Lấy chi tiết đơn hàng",
            description = "Trả về thông tin chi tiết của một đơn hàng theo ID. Có thể bao gồm đơn hàng đã bị xóa nếu truyền `includeDeleted=true`."
    )
    public ResponseEntity<Order> getOrderById(@PathVariable Long id,
                                              @RequestParam(defaultValue = "false") boolean includeDeleted) {
        return orderService.getOrderById(id, includeDeleted)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Tạo đơn hàng
    @PostMapping
    @Operation(
            summary = "Tạo đơn hàng mới",
            description = "Tạo một đơn hàng mới và lưu vào cơ sở dữ liệu."
    )
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        Order savedOrder = orderService.createOrderFromRequest(request);
        return ResponseEntity.ok(savedOrder);
    }

    // ✅ Cập nhật đơn hàng (PATCH)
    @PatchMapping("/{id}")
    @Operation(
            summary = "Cập nhật thông tin đơn hàng",
            description = "Cập nhật một phần thông tin của đơn hàng (PATCH)."
    )
    public ResponseEntity<Order> patchUpdateOrder(@PathVariable Long id, @RequestBody Order order) {
        Order updated = orderService.patchUpdateOrder(id, order);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // ✅ Xoá mềm đơn hàng
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Xóa mềm đơn hàng",
            description = "Đánh dấu đơn hàng là đã xóa mà không xóa khỏi cơ sở dữ liệu."
    )
    public ResponseEntity<String> softDeleteOrder(@PathVariable Long id) {
        boolean deleted = orderService.softDeleteOrder(id);
        return deleted ? ResponseEntity.ok("Order soft-deleted successfully") : ResponseEntity.notFound().build();
    }

    // ✅ Khôi phục đơn hàng đã xoá
    @PatchMapping("/{id}/restore")
    @Operation(
            summary = "Khôi phục đơn hàng đã xóa",
            description = "Khôi phục lại đơn hàng đã bị xóa mềm trước đó."
    )
    public ResponseEntity<String> restoreOrder(@PathVariable Long id) {
        boolean restored = orderService.restoreOrder(id);
        return restored ? ResponseEntity.ok("Order restored successfully") : ResponseEntity.notFound().build();
    }
}
