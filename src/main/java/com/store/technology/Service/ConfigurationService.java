package com.store.technology.Service;

import com.store.technology.Entity.Configuration;
import com.store.technology.Repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    // Lấy tất cả (trừ deleted vì @Where đã lọc)
    public List<Configuration> getAll() {
        return configurationRepository.findAll();
    }

    // Lấy theo ID (tự động bỏ deleted nhờ @Where)
    public Optional<Configuration> getById(Long id) {
        return configurationRepository.findById(id);
    }

    // Tạo mới
    public Configuration create(Configuration config) {
        return configurationRepository.save(config);
    }

    // Cập nhật
    public Configuration update(Long id, Configuration data) {
        Optional<Configuration> opt = configurationRepository.findById(id);
        if (opt.isEmpty()) return null;

        Configuration config = opt.get();
        if (data.getConfigKey() != null) {
            config.setConfigKey(data.getConfigKey());
        }
        if (data.getConfigValue() != null) {
            config.setConfigValue(data.getConfigValue());
        }

        return configurationRepository.save(config);
    }

    // Soft delete (sử dụng deletedAt)
    public boolean softDelete(Long id) {
        Optional<Configuration> opt = configurationRepository.findById(id);
        if (opt.isEmpty()) return false;

        Configuration config = opt.get();
        config.setDeletedAt(LocalDateTime.now());
        configurationRepository.save(config);
        return true;
    }

    // Khôi phục (set deletedAt = null)
    public boolean restore(Long id) {
        Optional<Configuration> opt = configurationRepository.findByIdIncludingDeleted(id);
        if (opt.isEmpty()) return false;

        Configuration config = opt.get();
        config.setDeletedAt(null);
        configurationRepository.save(config);
        return true;
    }
}
