package com.store.technology.Service;

import com.store.technology.Entity.Configuration;
import com.store.technology.Repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConfigurationService {
    @Autowired
    private ConfigurationRepository configurationRepository;

    // Lấy tất cả (kể cả xoá)
    public List<Configuration> getAll() {
        return configurationRepository.findAll();
    }

    // Lấy tất cả chưa bị xoá
    public List<Configuration> getActive() {
        return configurationRepository.findAllByDeletedAtIsNull();
    }

    // Lấy tất cả đã bị xoá
    public List<Configuration> getDeleted() {
        return configurationRepository.findAllByDeletedAtIsNotNull();
    }

    // Lấy 1 bản ghi (cả xoá)
    public Configuration getById(Long id) {
        return configurationRepository.findById(id).orElse(null);
    }

    // Lấy 1 bản ghi chưa xoá
    public Configuration getActiveById(Long id) {
        return configurationRepository.findByIdAndDeletedAtIsNull(id).orElse(null);
    }

    // Thêm mới
    public Configuration create(Configuration config) {
        return configurationRepository.save(config);
    }

    // Cập nhật (PATCH)
    public Configuration update(Long id, Configuration updatedConfig) {
        return configurationRepository.findById(id).map(c -> {
            if (updatedConfig.getName() != null) c.setName(updatedConfig.getName());
            if (updatedConfig.getDescription() != null) c.setDescription(updatedConfig.getDescription());
            return configurationRepository.save(c);
        }).orElse(null);
    }

    // Xoá mềm (gán thời điểm xoá)
    public boolean softDelete(Long id) {
        return configurationRepository.findById(id).map(c -> {
            c.setDeletedAt(LocalDateTime.now());
            configurationRepository.save(c);
            return true;
        }).orElse(false);
    }

    // Khôi phục (xóa dấu deleted_at)
    public boolean restore(Long id) {
        return configurationRepository.findById(id).map(c -> {
            c.setDeletedAt(null);
            configurationRepository.save(c);
            return true;
        }).orElse(false);
    }
}
