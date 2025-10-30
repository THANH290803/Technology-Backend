package com.store.technology.Controller;

import com.store.technology.Entity.Configuration;
import com.store.technology.Service.ConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Configurations", description = "CRUD API cho Configurations (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/configurations")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    // Lấy tất cả (kể cả xoá)
    @Operation(summary = "Lấy tất cả Configuration", description = "Lấy danh sách tất cả Configuration bao gồm cả những bản ghi đã bị xoá mềm")
    @GetMapping("/all")
    public List<Configuration> getAll() {
        return configurationService.getAll();
    }

    // Lấy chưa xoá
    @GetMapping
    @Operation(summary = "Lấy Configuration đang hoạt động", description = "Chỉ lấy các Configuration chưa bị xoá mềm")
    public List<Configuration> getActive() {
        return configurationService.getActive();
    }

    // Lấy đã xoá
    @GetMapping("/deleted")
    @Operation(summary = "Lấy Configuration đã xoá mềm", description = "Chỉ lấy các Configuration đã bị xoá mềm")
    public List<Configuration> getDeleted() {
        return configurationService.getDeleted();
    }

    // Lấy 1 (kể cả xoá)
    @GetMapping("/any/{id}")
    @Operation(summary = "Lấy Configuration theo ID", description = "Lấy 1 Configuration theo ID, bao gồm cả bản ghi đã bị xoá mềm")
    public Configuration getById(@PathVariable Long id) {
        return configurationService.getById(id);
    }

    // Lấy 1 chưa xoá
    @GetMapping("/{id}")
    @Operation(summary = "Lấy Configuration theo ID (chưa xoá mềm)", description = "Lấy 1 Configuration theo ID chỉ khi chưa bị xoá mềm")
    public Configuration getActiveById(@PathVariable Long id) {
        return configurationService.getActiveById(id);
    }

    // Thêm mới
    @PostMapping
    @Operation(summary = "Thêm mới Configuration", description = "Tạo mới một Configuration. Nếu có lỗi xảy ra, trả về thông tin chi tiết lỗi")
    public ResponseEntity<?> create(@RequestBody Configuration config) {
        try {
            Configuration saved = configurationService.create(config);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            // In ra console cho dev debug
            e.printStackTrace();

            // Trả thông báo lỗi cụ thể cho client
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Không thể thêm mới cấu hình",
                            "message", e.getMessage()
                    ));
        }
    }

    // Cập nhật (PATCH)
    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật Configuration", description = "Cập nhật thông tin của một Configuration theo ID")
    public Configuration update(@PathVariable Long id, @RequestBody Configuration configuration) {
        return configurationService.update(id, configuration);
    }

    // Xoá mềm
    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá mềm Configuration", description = "Đánh dấu Configuration là đã xoá (soft delete)")
    public String softDelete(@PathVariable Long id) {
        return configurationService.softDelete(id)
                ? "Deleted successfully"
                : "Configuration not found";
    }

    // Khôi phục
    @PatchMapping("/restore/{id}")
    @Operation(summary = "Khôi phục Configuration", description = "Khôi phục Configuration đã bị xoá mềm")
    public String restore(@PathVariable Long id) {
        return configurationService.restore(id)
                ? "Restored successfully"
                : "Configuration not found";
    }
}
