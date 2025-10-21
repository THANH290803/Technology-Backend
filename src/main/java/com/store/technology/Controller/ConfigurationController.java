package com.store.technology.Controller;

import com.store.technology.Entity.Configuration;
import com.store.technology.Service.ConfigurationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Configurations", description = "CRUD API cho Configurations (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/configurations")
@CrossOrigin("*")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    // Lấy tất cả (kể cả xoá)
    @GetMapping("/all")
    public List<Configuration> getAll() {
        return configurationService.getAll();
    }

    // Lấy chưa xoá
    @GetMapping
    public List<Configuration> getActive() {
        return configurationService.getActive();
    }

    // Lấy đã xoá
    @GetMapping("/deleted")
    public List<Configuration> getDeleted() {
        return configurationService.getDeleted();
    }

    // Lấy 1 (kể cả xoá)
    @GetMapping("/any/{id}")
    public Configuration getById(@PathVariable Long id) {
        return configurationService.getById(id);
    }

    // Lấy 1 chưa xoá
    @GetMapping("/{id}")
    public Configuration getActiveById(@PathVariable Long id) {
        return configurationService.getActiveById(id);
    }

    // Thêm mới
    @PostMapping
    public Configuration create(@RequestBody Configuration configuration) {
        return configurationService.create(configuration);
    }

    // Cập nhật (PATCH)
    @PatchMapping("/{id}")
    public Configuration update(@PathVariable Long id, @RequestBody Configuration configuration) {
        return configurationService.update(id, configuration);
    }

    // Xoá mềm
    @DeleteMapping("/{id}")
    public String softDelete(@PathVariable Long id) {
        return configurationService.softDelete(id)
                ? "Deleted successfully"
                : "Configuration not found";
    }

    // Khôi phục
    @PatchMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        return configurationService.restore(id)
                ? "Restored successfully"
                : "Configuration not found";
    }
}
