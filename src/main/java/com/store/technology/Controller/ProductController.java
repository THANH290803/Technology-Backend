package com.store.technology.Controller;

import com.store.technology.Entity.Product;
import com.store.technology.Service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product", description = "CRUD API cho Product (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {
    @Autowired
    private ProductService productService;

    // Hiển thị tất cả sản phẩm (kể cả đã xóa)
    @GetMapping("/all")
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    // Hiển thị các sản phẩm chưa bị xóa
    @GetMapping
    public List<Product> getActiveProducts() {
        return productService.getActiveProducts();
    }

    // Lấy 1 sản phẩm (kể cả bị xóa)
    @GetMapping("/any/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // Lấy 1 sản phẩm chưa bị xóa
    @GetMapping("/{id}")
    public Product getActiveById(@PathVariable Long id) {
        return productService.getActiveProductById(id);
    }

    // Thêm sản phẩm
    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    // Cập nhật (PATCH)
    @PatchMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    // Xóa mềm
    @DeleteMapping("/{id}")
    public String softDelete(@PathVariable Long id) {
        return productService.softDeleteProduct(id) ? "Deleted successfully" : "Product not found";
    }

    // Khôi phục sản phẩm
    @PatchMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        return productService.restoreProduct(id) ? "Restored successfully" : "Product not found";
    }
}
