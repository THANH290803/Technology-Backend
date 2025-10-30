package com.store.technology.Controller;

import com.store.technology.DTO.SpecificationRequest;
import com.store.technology.Entity.Specification;
import com.store.technology.Repository.ConfigurationRepository;
import com.store.technology.Service.SpecificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Specifications", description = "CRUD API cho Specifications (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/specifications")
public class SpecificationController {
    private final SpecificationService specificationService;

    public SpecificationController(SpecificationService specificationService) {
        this.specificationService = specificationService;
    }

    // 🔹 Lấy tất cả chưa xoá
    @GetMapping
    @Operation(summary = "Lấy danh sách Specification", description = "Chỉ lấy các Specification chưa bị xoá mềm")
    public ResponseEntity<List<Specification>> getAllNotDeleted() {
        return ResponseEntity.ok(specificationService.getAllNotDeleted());
    }

    // 🔹 Lấy tất cả bao gồm cả xoá mềm
    @GetMapping("/all")
    @Operation(summary = "Lấy tất cả Specification", description = "Bao gồm cả các Specification đã bị xoá mềm")
    public ResponseEntity<List<Specification>> getAllIncludingDeleted() {
        return ResponseEntity.ok(specificationService.getAllIncludingDeleted());
    }

    // 🔹 Lấy 1 bản ghi
    @GetMapping("/{id}")
    @Operation(summary = "Lấy Specification theo ID", description = "Có thể bao gồm cả bản ghi đã bị xoá mềm nếu includeDeleted=true")
    public ResponseEntity<Specification> getById(@PathVariable Long id,
                                                 @RequestParam(defaultValue = "false") boolean includeDeleted) {
        return ResponseEntity.ok(specificationService.getById(id, includeDeleted));
    }

    // 🔹 Thêm mới
    @PostMapping
    @Operation(summary = "Thêm mới Specification", description = "Tạo Specification mới, yêu cầu truyền name và configurationId")
    public ResponseEntity<?> create(@RequestBody SpecificationRequest request) {
        try {
            Specification newSpec = specificationService.createFromJson(request.getName(), request.getValue(), request.getConfigurationId());
            Map<String, Object> res = new HashMap<>();
            res.put("message", "Tạo Specification thành công!");
            res.put("data", newSpec);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return buildError(e);
        }
    }

    // 🔹 Cập nhật
    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật Specification", description = "Cập nhật name và configurationId theo ID")
    public ResponseEntity<?> update(@PathVariable(required = false) Long id, @RequestBody SpecificationRequest request) {
        try {
            if (id == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "message", "Thiếu ID trong URL (ví dụ: /api/specifications/1)"
                ));
            }

            Specification updated = specificationService.updateFromJson(id, request.getName(), request.getValue(), request.getConfigurationId());
            return ResponseEntity.ok(Map.of(
                    "message", "Cập nhật Specification thành công!",
                    "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", e.getClass().getSimpleName(),
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/configuration/{configurationId}")
    @Operation(summary = "Lấy Specification theo Configuration Id", description = "Lấy Specification theo Configuration Id")
    public ResponseEntity<List<Specification>> getByConfiguration(@PathVariable Long configurationId,
                                                                  @RequestParam(defaultValue = "false") boolean includeDeleted) {
        List<Specification> specs = specificationService.getByConfigurationId(configurationId, includeDeleted);
        return ResponseEntity.ok(specs);
    }


    // 🔹 Xoá mềm
    @PatchMapping("/{id}/delete")
    @Operation(summary = "Xoá mềm Specification", description = "Đánh dấu Specification là đã xoá (soft delete)")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        specificationService.softDelete(id);
        return ResponseEntity.ok().build();
    }

    // 🔹 Khôi phục
    @PatchMapping("/{id}/restore")
    @Operation(summary = "Khôi phục Specification", description = "Khôi phục Specification đã bị xoá mềm")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        specificationService.restore(id);
        return ResponseEntity.ok().build();
    }

    // ⚙️ Hàm xử lý lỗi dùng chung
    private ResponseEntity<Map<String, Object>> buildError(Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", e.getClass().getSimpleName());
        error.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
