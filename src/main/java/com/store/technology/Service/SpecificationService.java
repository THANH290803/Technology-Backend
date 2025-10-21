package com.store.technology.Service;

import com.store.technology.Entity.Specification;
import com.store.technology.Repository.SpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpecificationService {
    @Autowired
    private SpecificationRepository specificationRepository;

    // Lấy tất cả (kể cả xoá)
    public List<Specification> findAllIncludingDeleted() {
        return specificationRepository.findAllIncludingDeleted();
    }

    // Lấy tất cả chưa xoá
    public List<Specification> findAllNotDeleted() {
        return specificationRepository.findAllNotDeleted();
    }

    // Lấy 1 (kể cả xoá)
    public Specification findAnyById(Long id) {
        return specificationRepository.findAnyById(id);
    }

    // Lấy 1 (chưa xoá)
    public Specification findNotDeletedById(Long id) {
        return specificationRepository.findNotDeletedById(id);
    }

    // Thêm mới
    public Specification save(Specification specification) {
        return specificationRepository.save(specification);
    }

    // Cập nhật (PATCH)
    public Specification update(Long id, Specification updatedSpec) {
        Specification existing = specificationRepository.findAnyById(id);
        if (existing == null) {
            throw new RuntimeException("Không tìm thấy specification id: " + id);
        }

        if (updatedSpec.getName() != null) {
            existing.setName(updatedSpec.getName());
        }
        if (updatedSpec.getConfiguration() != null) {
            existing.setConfiguration(updatedSpec.getConfiguration());
        }

        return specificationRepository.save(existing);
    }

    // Xoá mềm
    public void softDelete(Long id) {
        Specification spec = specificationRepository.findAnyById(id);
        if (spec != null) {
            spec.setDeletedAt(LocalDateTime.now());
            specificationRepository.save(spec);
        }
    }

    // Khôi phục
    public void restore(Long id) {
        Specification spec = specificationRepository.findAnyById(id);
        if (spec != null) {
            spec.setDeletedAt(null);
            specificationRepository.save(spec);
        }
    }
}
