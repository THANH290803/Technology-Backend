package com.store.technology.Controller;

import com.store.technology.Entity.Role;
import com.store.technology.Service.BrandService;
import com.store.technology.Service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Role", description = "CRUD API cho Role (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Lấy tất cả roles chưa xoá
    @GetMapping
    @Operation(summary = "Lấy danh sách role", description = "Chỉ lấy role chưa bị xoá")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    // Lấy tất cả roles kể cả đã xoá
    @GetMapping("/all")
    @Operation(summary = "Lấy danh sách role (bao gồm đã xoá)", description = "Admin có thể xem tất cả roles")
    public ResponseEntity<List<Role>> getAllIncludingDeleted() {
        return ResponseEntity.ok(roleService.findAllIncludingDeleted());
    }

    // Lấy 1 role theo id
    @GetMapping("/{id}")
    @Operation(summary = "Lấy role theo ID", description = "Chỉ lấy role chưa xoá")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Tạo role mới
    @PostMapping
    @Operation(summary = "Tạo role mới")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.save(role));
    }

    // Update role
    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật một phần role")
    public ResponseEntity<Role> patchRole(
            @PathVariable Long id,
            @RequestBody Role rolePatch) {
        return ResponseEntity.ok(roleService.patch(id, rolePatch));
    }

    // Restore role
    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục role", description = "Gán lại deleted_at = NULL để khôi phục")
    public ResponseEntity<Role> restoreRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.restore(id));
    }

    // Soft delete role
    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá mềm role", description = "Soft delete bằng cách set deleted_at")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
