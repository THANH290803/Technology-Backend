package com.store.technology.Service;

import com.store.technology.Entity.Specification;
import com.store.technology.Repository.SpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpecificationService {

    @Autowired
    private SpecificationRepository specificationRepository;

    //Lấy tất cả chưa bị xóa mềm (lọc trong Java)
    public List<Specification> getAll() {
        return specificationRepository.findAllIncludingDeleted()
                .stream()
                .filter(spec -> spec.getDeletedAt() == null)
                .collect(Collectors.toList());
    }

    //Lấy 1 bản ghi chưa bị xóa mềm
    public Optional<Specification> getById(Long id) {
        return specificationRepository.findByIdIncludingDeleted(id)
                .filter(spec -> spec.getDeletedAt() == null);
    }

    //Tạo mới
    public Specification create(Specification spec) {
        return specificationRepository.save(spec);
    }

    //Cập nhật
    public Specification update(Long id, Specification data) {
        Optional<Specification> opt = specificationRepository.findByIdIncludingDeleted(id)
                .filter(spec -> spec.getDeletedAt() == null);
        if (opt.isEmpty()) return null;

        Specification spec = opt.get();
        spec.setKeyName(data.getKeyName());
        spec.setValue(data.getValue());
        return specificationRepository.save(spec);
    }

    //Xóa mềm
    public boolean softDelete(Long id) {
        Optional<Specification> opt = specificationRepository.findByIdIncludingDeleted(id)
                .filter(spec -> spec.getDeletedAt() == null);
        if (opt.isEmpty()) return false;

        Specification spec = opt.get();
        spec.setDeletedAt(LocalDateTime.now());
        specificationRepository.save(spec);
        return true;
    }

    //Khôi phục
    public boolean restore(Long id) {
        Optional<Specification> opt = specificationRepository.findByIdIncludingDeleted(id);
        if (opt.isEmpty()) return false;

        Specification spec = opt.get();
        spec.setDeletedAt(null);
        specificationRepository.save(spec);
        return true;
    }
}
