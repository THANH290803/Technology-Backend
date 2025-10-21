package com.store.technology.Service;

import com.store.technology.Entity.ProductDetail;
import com.store.technology.Repository.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductDetailService {
    @Autowired
    private ProductDetailRepository productDetailRepository;

    // Lấy tất cả (kể cả xoá)
    public List<ProductDetail> findAllIncludingDeleted() {
        return productDetailRepository.findAllIncludingDeleted();
    }

    // Lấy tất cả chưa xoá
    public List<ProductDetail> findAllNotDeleted() {
        return productDetailRepository.findAllNotDeleted();
    }

    // Lấy 1 (kể cả xoá)
    public ProductDetail findAnyById(Long id) {
        return productDetailRepository.findAnyById(id);
    }

    // Lấy 1 (chưa xoá)
    public ProductDetail findNotDeletedById(Long id) {
        return productDetailRepository.findNotDeletedById(id);
    }

    // Thêm
    public ProductDetail save(ProductDetail productDetail) {
        return productDetailRepository.save(productDetail);
    }

    // Cập nhật (PATCH)
    public ProductDetail update(Long id, ProductDetail updatedDetail) {
        ProductDetail existing = productDetailRepository.findAnyById(id);
        if (existing == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm chi tiết id: " + id);
        }

        if (updatedDetail.getQuantity() != null) {
            existing.setQuantity(updatedDetail.getQuantity());
        }
        if (updatedDetail.getPrice() != null) {
            existing.setPrice(updatedDetail.getPrice());
        }
        if (updatedDetail.getConfiguration() != null) {
            existing.setConfiguration(updatedDetail.getConfiguration());
        }
        if (updatedDetail.getProduct() != null) {
            existing.setProduct(updatedDetail.getProduct());
        }

        return productDetailRepository.save(existing);
    }

    // Xoá mềm
    public void softDelete(Long id) {
        ProductDetail detail = productDetailRepository.findAnyById(id);
        if (detail != null) {
            detail.setDeletedAt(LocalDateTime.now());
            productDetailRepository.save(detail);
        }
    }

    // Khôi phục
    public void restore(Long id) {
        ProductDetail detail = productDetailRepository.findAnyById(id);
        if (detail != null) {
            detail.setDeletedAt(null);
            productDetailRepository.save(detail);
        }
    }
}
