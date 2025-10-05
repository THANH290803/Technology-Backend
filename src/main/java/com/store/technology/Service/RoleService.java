package com.store.technology.Service;

import com.store.technology.Entity.Brand;
import com.store.technology.Entity.Role;
import com.store.technology.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Lấy tất cả role (chỉ lấy role chưa bị soft delete do @Where)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    // Lấy tất cả brand (bao gồm cả xoá)
    public List<Role> findAllIncludingDeleted() {
        return roleRepository.findAllIncludingDeleted();
    }

    // Tạo mới role
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    // Lấy 1 role theo id
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    // Update brand
    public Role patch(Long id, Role updatedRole) {
        return roleRepository.findById(id)
                .map(brand -> {
                    if (updatedRole.getName() != null) {
                        brand.setName(updatedRole.getName());
                    }
                    if (updatedRole.getDescription() != null) {
                        brand.setDescription(updatedRole.getDescription());
                    }
                    return roleRepository.save(brand);
                })
                .orElseThrow(() -> new RuntimeException("Role not found with id " + id));
    }

    // Soft delete (sẽ set deleted_at)
    public void softDelete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id " + id));
        roleRepository.delete(role);
    }

    // Restore (set deletedAt = null)
    public Role restore(Long id) {
        Role role  = roleRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new RuntimeException("Brand not found with id " + id));
        role.setDeletedAt(null);
        return roleRepository.save(role);
    }
}
