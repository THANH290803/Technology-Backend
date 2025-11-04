package com.store.technology.Controller;

import com.store.technology.DTO.ProductDetailRequest;
import com.store.technology.Entity.ProductDetail;
import com.store.technology.Service.ProductDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Product Detail", description = "CRUD API cho Product Detail (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/product-details")
@CrossOrigin("*")
public class ProductDetailController {
    private final ProductDetailService service;

    public ProductDetailController(ProductDetailService service) {
        this.service = service;
    }

    // 🔹 Lấy tất cả (chưa xoá mềm)
    @GetMapping
    @Operation(summary = "Lấy danh sách Product Detail", description = "Chỉ lấy các Product Detail chưa bị xoá mềm")
    public List<ProductDetail> getAll() {
        return service.getAllNotDeleted();
    }

    // 🔹 Lấy tất cả (bao gồm xoá mềm)
    @GetMapping("/all")
    @Operation(summary = "Lấy tất cả Product Detail", description = "Bao gồm cả các Product Detail đã bị xoá mềm")
    public List<ProductDetail> getAllIncludingDeleted() {
        return service.getAllIncludingDeleted();
    }

    // 🔹 Lấy 1 sản phẩm chi tiết (chưa xoá mềm)
    @GetMapping("/{id}")
    @Operation(summary = "Lấy Product Detail theo ID", description = "Chỉ lấy Product Detail chưa bị xoá mềm")
    public ResponseEntity<ProductDetail> getNotDeletedById(@PathVariable Long id) {
        ProductDetail detail = service.getById(id, false);
        return detail != null ? ResponseEntity.ok(detail) : ResponseEntity.notFound().build();
    }

    // 🔹 Lấy 1 sản phẩm chi tiết (bao gồm xoá mềm)
    @GetMapping("/{id}/any")
    @Operation(summary = "Lấy Product Detail (bao gồm cả xoá mềm)", description = "Lấy Product Detail theo ID, bao gồm cả bản ghi đã bị xoá mềm")
    public ResponseEntity<ProductDetail> getAnyById(@PathVariable Long id) {
        ProductDetail detail = service.getById(id, true);
        return detail != null ? ResponseEntity.ok(detail) : ResponseEntity.notFound().build();
    }

    // 🔹 Lấy danh sách ProductDetail theo productId
    @GetMapping("/product/{productId}")
    @Operation(summary = "Lấy danh sách Product Detail theo productId", description = "Chỉ lấy các Product Detail chưa bị xoá mềm của product cụ thể")
    public ResponseEntity<List<ProductDetail>> getByProductId(@PathVariable Long productId) {
        List<ProductDetail> details = service.getByProductId(productId);
        return ResponseEntity.ok(details);
    }

    // 🔹 Thêm mới
    @PostMapping
    @Operation(summary = "Thêm mới Product Detail", description = "Tạo Product Detail mới với configurationId, productId, quantity, price")
    public ResponseEntity<?> create(@RequestBody ProductDetailRequest request) {
        try {
            ProductDetail detail = service.createFromJson(
                    request.getConfigurationId(),
                    request.getProductId(),
                    request.getQuantity(),
                    request.getPrice()
            );
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Tạo Product Detail thành công!",
                    "data", detail
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "error", e.getClass().getSimpleName(),
                    "message", e.getMessage()
            ));
        }
    }

    // 🔹 Cập nhật
    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật Product Detail", description = "Cập nhật thông tin Product Detail theo ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDetailRequest request) {
        try {
            ProductDetail updated = service.updateFromJson(
                    id,
                    request.getConfigurationId(),
                    request.getProductId(),
                    request.getQuantity(),
                    request.getPrice()
            );
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cập nhật Product Detail thành công!",
                    "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "error", e.getClass().getSimpleName(),
                    "message", e.getMessage()
            ));
        }
    }

    // 🔹 Xoá mềm
    @PatchMapping("/{id}/delete")
    @Operation(summary = "Xoá mềm Product Detail", description = "Đánh dấu Product Detail là đã xoá (soft delete)")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Khôi phục
    @PatchMapping("/{id}/restore")
    @Operation(summary = "Khôi phục Product Detail", description = "Khôi phục Product Detail đã bị xoá mềm")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        service.restore(id);
        return ResponseEntity.noContent().build();
    }
}
