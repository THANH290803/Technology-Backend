package com.store.technology.Controller;

import com.store.technology.Entity.Configuration;
import com.store.technology.Service.ConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Configuration", description = "CRUD API cho cấu hình hệ thống (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/configurations")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách cấu hình chưa xoá")
    public ResponseEntity<List<Configuration>> getAll() {
        return ResponseEntity.ok(configurationService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy cấu hình theo ID")
    public ResponseEntity<Configuration> getById(@PathVariable Long id) {
        Optional<Configuration> opt = configurationService.getById(id);
        return opt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Tạo cấu hình mới")
    public ResponseEntity<Configuration> create(@RequestBody Configuration config) {
        return ResponseEntity.ok(configurationService.create(config));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật cấu hình")
    public ResponseEntity<Configuration> update(@PathVariable Long id, @RequestBody Configuration config) {
        Configuration updated = configurationService.update(id, config);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá mềm cấu hình")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        boolean result = configurationService.softDelete(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục cấu hình đã xoá")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        boolean result = configurationService.restore(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
