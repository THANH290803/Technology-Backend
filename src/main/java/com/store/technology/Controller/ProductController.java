package com.store.technology.Controller;

import com.store.technology.DTO.ProductRequest;
import com.store.technology.Entity.Product;
import com.store.technology.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "Product", description = "CRUD API cho Product (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {
    @Autowired
    private ProductService productService;

    // Hiển thị tất cả sản phẩm (kể cả đã xóa)
    @GetMapping("/all")
    @Operation(summary = "Lấy tất cả Product", description = "Lấy danh sách tất cả Product bao gồm cả các bản ghi đã bị xoá mềm")
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    // Hiển thị các sản phẩm chưa bị xóa
    @GetMapping
    @Operation(summary = "Lấy Product đang hoạt động", description = "Chỉ lấy các Product chưa bị xoá mềm")
    public List<Product> getActiveProducts() {
        return productService.getActiveProducts();
    }

    // Lấy 1 sản phẩm (kể cả bị xóa)
    @GetMapping("/any/{id}")
    @Operation(summary = "Lấy Product theo ID", description = "Lấy một Product theo ID, bao gồm cả bản ghi đã bị xoá mềm")
    public Product getById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // Lấy 1 sản phẩm chưa bị xóa
    @GetMapping("/{id}")
    @Operation(summary = "Lấy Product theo ID (chưa xoá mềm)", description = "Lấy một Product theo ID chỉ khi chưa bị xoá mềm")
    public Product getActiveById(@PathVariable Long id) {
        return productService.getActiveProductById(id);
    }

    // ✅ Tạo sản phẩm mới
    @PostMapping
    @Operation(summary = "Thêm mới Product", description = "Tạo Product mới từ name, totalQuality, brandId, categoryId")
    public ResponseEntity<?> create(@RequestBody ProductRequest request) {
        try {
            Product product = productService.createProduct(request);
            return ResponseEntity.ok(Map.of(
                    "message", "Thêm sản phẩm thành công!",
                    "data", product
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", e.getClass().getSimpleName(),
                    "message", e.getMessage()
            ));
        }
    }

    // ✅ Cập nhật sản phẩm
    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật Product", description = "Cập nhật thông tin Product theo ID")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductRequest request) {
        try {
            Product updated = productService.updateProduct(id, request);
            return ResponseEntity.ok(Map.of(
                    "message", "Cập nhật sản phẩm thành công!",
                    "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", e.getClass().getSimpleName(),
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/create-product-productDetails")
    @Operation(
            summary = "Thêm mới sản phẩm",
            description = """
        Tạo mới một sản phẩm cùng danh sách chi tiết cấu hình (ProductDetails).
        - Tự động sinh `productCode`.
        - Nhận vào danh sách cấu hình gồm `configurationId`, `quantity`, `price`.
        """
    )
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
        Product product = productService.createProductWithDetails(request);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/updateProductWithProductDetail/{id}")
    // 🟡 Cập nhật sản phẩm (PATCH)
    @Operation(summary = "Cập nhật sản phẩm", description = "Cập nhật thông tin sản phẩm và chi tiết sản phẩm theo ID.")
    public ResponseEntity<Product> updateProductWithProductDetail(@PathVariable Long id, @RequestBody ProductRequest request) {
        Product updated = productService.updateProductWithProductDetail(id, request);
        return ResponseEntity.ok(updated);
    }

    // Xóa mềm
    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá mềm Product", description = "Đánh dấu Product là đã xoá (soft delete)")
    public String softDelete(@PathVariable Long id) {
        return productService.softDeleteProduct(id) ? "Deleted successfully" : "Product not found";
    }

    // Khôi phục sản phẩm
    @PatchMapping("/restore/{id}")
    @Operation(summary = "Khôi phục Product", description = "Khôi phục Product đã bị xoá mềm")
    public String restore(@PathVariable Long id) {
        return productService.restoreProduct(id) ? "Restored successfully" : "Product not found";
    }

    @PostMapping("/import")
    public ResponseEntity<String> importProducts(@RequestParam("file") MultipartFile file) {
        try {
            String filename = file.getOriginalFilename().toLowerCase();
            if (filename.endsWith(".csv")) {
                productService.importFromCSV(file);
            } else if (filename.endsWith(".xls") || filename.endsWith(".xlsx")) {
                productService.importFromExcel(file);
            } else {
                return ResponseEntity.badRequest().body("File không hợp lệ (chỉ CSV hoặc Excel)");
            }
            return ResponseEntity.ok("Import thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi import: " + e.getMessage());
        }
    }
}
