package com.store.technology.Controller;

import com.store.technology.DTO.CategoryRequest;
import com.store.technology.Entity.Category;
import com.store.technology.Service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Category", description = "CRUD API cho Role (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    // Lấy tất cả category chưa bị xóa
    @GetMapping("/")
    @Operation(summary = "Lấy danh sách category chưa bị xoá",
            description = "Trả về tất cả category có deleted_at IS NULL")
    public ResponseEntity<List<Category>> getAllActiveCategories() {
        return ResponseEntity.ok(categoryService.getAllActive());
    }

    // Lấy tất cả category kể cả đã xóa
    @Operation(summary = "Lấy tất cả category (kể cả đã xoá)",
            description = "Trả về danh sách tất cả category bao gồm cả đã bị xoá mềm")
    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllWithDeleted() {
        return ResponseEntity.ok(categoryService.getAllWithDeleted());
    }

    // Lấy category theo id (chưa bị xóa)
    @Operation(summary = "Lấy category theo ID (chưa bị xoá)",
            description = "Trả về category cụ thể nếu chưa bị xoá (deleted_at IS NULL)")
    @GetMapping("/{id}")
    public ResponseEntity<?> getActiveById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getActiveById(id);
        return category.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("Không tìm thấy category hoặc đã bị xóa"));
    }

    // Lấy category theo id (kể cả đã xóa)
    @GetMapping("/with-deleted/{id}")
    @Operation(summary = "Lấy category theo ID (kể cả đã xoá)",
            description = "Trả về category cụ thể kể cả đã bị xoá mềm (deleted_at IS NOT NULL)")
    public ResponseEntity<?> getByIdIncludingDeleted(@PathVariable Long id) {
        Optional<Category> category = categoryService.getByIdIncludingDeleted(id);
        return category.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("Không tìm thấy category"));
    }

    // Tạo category mới
    @PostMapping("/create")
    @Operation(summary = "Tạo mới category", description = "Thêm category mới với brandId")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    // Cập nhật category (PATCH)
    @Operation(summary = "Cập nhật category",
            description = "Cập nhật thông tin name, description hoặc brand cho category có ID cụ thể")
    @PatchMapping("/update/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    // Soft delete category
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Xoá mềm category",
            description = "Đánh dấu category là đã xoá bằng cách set deleted_at = thời gian hiện tại")
    public ResponseEntity<String> softDelete(@PathVariable Long id) {
        String message = categoryService.softDelete(id);
        return ResponseEntity.ok(message);
    }

    // Restore category
    @Operation(summary = "Khôi phục category đã xoá",
            description = "Đặt lại deleted_at = NULL để khôi phục category đã bị xoá mềm")
    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restore(@PathVariable Long id) {
        String message = categoryService.restore(id);
        return ResponseEntity.ok(message);
    }
}
