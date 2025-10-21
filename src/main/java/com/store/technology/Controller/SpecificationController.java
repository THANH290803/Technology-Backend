package com.store.technology.Controller;

import com.store.technology.Entity.Specification;
import com.store.technology.Service.SpecificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Specifications", description = "CRUD API cho Specifications (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/specifications")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    // 🔹 Lấy tất cả (kể cả xoá)
    @GetMapping("/all")
    public List<Specification> getAllIncludingDeleted() {
        return specificationService.findAllIncludingDeleted();
    }

    // 🔹 Lấy tất cả chưa xoá
    @GetMapping
    public List<Specification> getAllNotDeleted() {
        return specificationService.findAllNotDeleted();
    }

    // 🔹 Lấy 1 (kể cả xoá)
    @GetMapping("/{id}/all")
    public Specification getAnyById(@PathVariable Long id) {
        return specificationService.findAnyById(id);
    }

    // 🔹 Lấy 1 (chưa xoá)
    @GetMapping("/{id}")
    public Specification getNotDeletedById(@PathVariable Long id) {
        return specificationService.findNotDeletedById(id);
    }

    // 🔹 Thêm mới
    @PostMapping
    public Specification create(@RequestBody Specification specification) {
        return specificationService.save(specification);
    }

    // 🔹 Cập nhật (PATCH)
    @PatchMapping("/{id}")
    public Specification update(@PathVariable Long id, @RequestBody Specification updatedSpec) {
        return specificationService.update(id, updatedSpec);
    }

    // 🔹 Xoá mềm
    @DeleteMapping("/{id}")
    public void softDelete(@PathVariable Long id) {
        specificationService.softDelete(id);
    }

    // 🔹 Khôi phục
    @PutMapping("/{id}/restore")
    public void restore(@PathVariable Long id) {
        specificationService.restore(id);
    }
}
