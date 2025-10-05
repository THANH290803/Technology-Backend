package com.store.technology.Controller;

import com.store.technology.Entity.Brand;
import com.store.technology.Service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Brand", description = "CRUD API cho Brand (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/brands")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    // Lấy tất cả brands chưa xoá
    @GetMapping
    @Operation(summary = "Lấy danh sách brand", description = "Chỉ lấy brand chưa bị xoá")
    public ResponseEntity<List<Brand>> getAllBrands() {
        return ResponseEntity.ok(brandService.findAll());
    }

    // Lấy tất cả brands kể cả đã xoá
    @GetMapping("/all")
    @Operation(summary = "Lấy danh sách brand (bao gồm đã xoá)", description = "Admin có thể xem tất cả brands")
    public ResponseEntity<List<Brand>> getAllIncludingDeleted() {
        return ResponseEntity.ok(brandService.findAllIncludingDeleted());
    }

    // Lấy 1 brand theo id
    @GetMapping("/{id}")
    @Operation(summary = "Lấy brand theo ID", description = "Chỉ lấy brand chưa xoá")
    public ResponseEntity<Brand> getBrandById(@PathVariable Long id) {
        return brandService.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo brand mới
    @PostMapping
    @Operation(summary = "Tạo brand mới")
    public ResponseEntity<Brand> createBrand(@RequestBody Brand brand) {
        return ResponseEntity.ok(brandService.save(brand));
    }

    // Update brand
    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật một phần brand")
    public ResponseEntity<Brand> patchBrand(
            @PathVariable Long id,
            @RequestBody Brand brandPatch) {
        return ResponseEntity.ok(brandService.patch(id, brandPatch));
    }

    // Restore brand
    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục brand", description = "Gán lại deleted_at = NULL để khôi phục")
    public ResponseEntity<Brand> restoreBrand(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.restore(id));
    }

    // Soft delete brand
    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá mềm brand", description = "Soft delete bằng cách set deleted_at")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
