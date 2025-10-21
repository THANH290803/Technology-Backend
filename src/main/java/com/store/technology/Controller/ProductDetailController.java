package com.store.technology.Controller;

import com.store.technology.Entity.ProductDetail;
import com.store.technology.Service.ProductDetailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product Detail", description = "CRUD API cho Product Detail (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/product-details")
@CrossOrigin("*")
public class ProductDetailController {
    @Autowired
    private ProductDetailService productDetailService;

    // 🔹 Lấy tất cả (kể cả xoá)
    @GetMapping("/all")
    public List<ProductDetail> getAllIncludingDeleted() {
        return productDetailService.findAllIncludingDeleted();
    }

    // 🔹 Lấy tất cả chưa xoá
    @GetMapping
    public List<ProductDetail> getAllNotDeleted() {
        return productDetailService.findAllNotDeleted();
    }

    // 🔹 Lấy 1 (kể cả xoá)
    @GetMapping("/{id}/all")
    public ProductDetail getAnyById(@PathVariable Long id) {
        return productDetailService.findAnyById(id);
    }

    // 🔹 Lấy 1 (chưa xoá)
    @GetMapping("/{id}")
    public ProductDetail getNotDeletedById(@PathVariable Long id) {
        return productDetailService.findNotDeletedById(id);
    }

    // 🔹 Thêm mới
    @PostMapping
    public ProductDetail create(@RequestBody ProductDetail productDetail) {
        return productDetailService.save(productDetail);
    }

    // 🔹 Cập nhật (PATCH)
    @PatchMapping("/{id}")
    public ProductDetail update(@PathVariable Long id, @RequestBody ProductDetail updatedDetail) {
        return productDetailService.update(id, updatedDetail);
    }

    // 🔹 Xoá mềm
    @DeleteMapping("/{id}")
    public void softDelete(@PathVariable Long id) {
        productDetailService.softDelete(id);
    }

    // 🔹 Khôi phục
    @PutMapping("/{id}/restore")
    public void restore(@PathVariable Long id) {
        productDetailService.restore(id);
    }
}
