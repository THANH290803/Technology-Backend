package com.store.technology.Service;

import com.store.technology.Entity.Brand;
import com.store.technology.Repository.BrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    private BrandRepository brandRepository;
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    // Lấy tất cả brand (chỉ lấy brand chưa bị soft delete do @Where)
    public List<Brand> findAll() {
        return brandRepository.findAll(); // @Where lọc sẵn deletedAt IS NULL
    }

    // Lấy tất cả brand (bao gồm cả xoá)
    public List<Brand> findAllIncludingDeleted() {
        return brandRepository.findAllIncludingDeleted();
    }

    // Tạo mới brand
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    // Lấy 1 brand theo id
    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    // Update brand
    public Brand patch(Long id, Brand updatedBrand) {
        return brandRepository.findById(id)
                .map(brand -> {
                    if (updatedBrand.getName() != null) {
                        brand.setName(updatedBrand.getName());
                    }
                    if (updatedBrand.getDescription() != null) {
                        brand.setDescription(updatedBrand.getDescription());
                    }
                    return brandRepository.save(brand);
                })
                .orElseThrow(() -> new RuntimeException("Brand not found with id " + id));
    }

    // Soft delete (sẽ set deleted_at)
    public void softDelete(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found with id " + id));
        brandRepository.delete(brand);
    }

    // Restore (set deletedAt = null)
    public Brand restore(Long id) {
        Brand brand  = brandRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new RuntimeException("Brand not found with id " + id));
        brand.setDeletedAt(null);
        return brandRepository.save(brand);
    }
}
