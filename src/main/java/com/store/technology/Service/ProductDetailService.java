package com.store.technology.Service;

import com.store.technology.Entity.ProductDetail;
import com.store.technology.Repository.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    public List<ProductDetail> getAll() {
        return productDetailRepository.findAllIncludingDeleted();
    }

    public Optional<ProductDetail> getById(Long id) {
        return productDetailRepository.findByIdIncludingDeleted(id);
    }

    public ProductDetail create(ProductDetail detail) {
        // Không có createdAt nên bỏ dòng này
        return productDetailRepository.save(detail);
    }

    public ProductDetail update(Long id, ProductDetail detailData) {
        Optional<ProductDetail> detailOpt = productDetailRepository.findByIdIncludingDeleted(id);
        if (detailOpt.isEmpty()) return null;

        ProductDetail detail = detailOpt.get();
        detail.setColor(detailData.getColor());
        detail.setSize(detailData.getSize());
        detail.setImageUrl(detailData.getImageUrl());

        // Không có updatedAt nên bỏ dòng này
        return productDetailRepository.save(detail);
    }

    public boolean softDelete(Long id) {
        Optional<ProductDetail> detailOpt = productDetailRepository.findByIdIncludingDeleted(id);
        if (detailOpt.isEmpty()) return false;

        ProductDetail detail = detailOpt.get();
        detail.setDeletedAt(LocalDateTime.now());
        productDetailRepository.save(detail);
        return true;
    }

    public boolean restore(Long id) {
        Optional<ProductDetail> detailOpt = productDetailRepository.findByIdIncludingDeleted(id);
        if (detailOpt.isEmpty()) return false;

        ProductDetail detail = detailOpt.get();
        detail.setDeletedAt(null);
        productDetailRepository.save(detail);
        return true;
    }
}
