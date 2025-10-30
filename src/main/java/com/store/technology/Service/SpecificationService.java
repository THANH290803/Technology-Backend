package com.store.technology.Service;

import com.store.technology.Entity.Configuration;
import com.store.technology.Entity.Specification;
import com.store.technology.Repository.ConfigurationRepository;
import com.store.technology.Repository.SpecificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpecificationService {
    private final SpecificationRepository repository;
    private final ConfigurationRepository configurationRepository;

    public SpecificationService(SpecificationRepository repository, ConfigurationRepository configurationRepository) {
        this.repository = repository;
        this.configurationRepository = configurationRepository;
    }

    public List<Specification> getAllNotDeleted() {
        return repository.findAllNotDeleted();
    }

    public List<Specification> getAllIncludingDeleted() {
        return repository.findAllIncludingDeleted();
    }

    public Specification getById(Long id, boolean includeDeleted) {
        return includeDeleted ? repository.findAnyById(id) : repository.findNotDeletedById(id);
    }

    public Specification save(Specification specification) {
        return repository.save(specification);
    }

    // ✅ Thêm mới từ JSON
    public Specification createFromJson(String name, String value, Long configurationId) {
        Configuration config = configurationRepository.findById(configurationId).orElse(null);
        if (config == null) return null;

        Specification spec = new Specification();
        spec.setName(name);
        spec.setValue(value);
        spec.setConfiguration(config);
        return repository.save(spec);
    }

    public Specification updateFromJson(Long id, String name, String value, Long configurationId) {
        Specification existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Specification có id = " + id));

        // 🟢 Chỉ cập nhật field nào có dữ liệu
        if (name != null && !name.isBlank()) {
            existing.setName(name);
        }
        if (value != null && !value.isBlank()) {
            existing.setValue(value);
        }
        if (configurationId != null) {
            Configuration config = configurationRepository.findById(configurationId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Configuration có id = " + configurationId));
            existing.setConfiguration(config);
        }

        return repository.save(existing);
    }

    // ✅ Xoá mềm
    public void softDelete(Long id) {
        Specification existing = repository.findNotDeletedById(id);
        if (existing != null) {
            existing.setDeletedAt(LocalDateTime.now());
            repository.save(existing);
        }
    }

    // ✅ Khôi phục
    public void restore(Long id) {
        Specification existing = repository.findAnyById(id);
        if (existing != null && existing.getDeletedAt() != null) {
            existing.setDeletedAt(null);
            repository.save(existing);
        }
    }
}
