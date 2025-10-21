package com.store.technology.Service;

import com.store.technology.Entity.Product;
import com.store.technology.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    // Lấy tất cả bao gồm cả bị xóa
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Lấy các sản phẩm chưa bị xóa
    public List<Product> getActiveProducts() {
        return productRepository.findAllByIsDeleted(false);
    }

    // Lấy 1 sản phẩm (cả khi bị xóa)
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    // Lấy 1 sản phẩm chưa bị xóa
    public Product getActiveProductById(Long id) {
        return productRepository.findByIdAndIsDeleted(id, false).orElse(null);
    }

    // Thêm mới
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Cập nhật bằng PATCH
    public Product updateProduct(Long id, Product updatedData) {
        return productRepository.findById(id).map(p -> {
            if (updatedData.getName() != null) p.setName(updatedData.getName());
            if (updatedData.getTotalQuality() != null) p.setTotalQuality(updatedData.getTotalQuality());
            if (updatedData.getBrandId() != null) p.setBrandId(updatedData.getBrandId());
            if (updatedData.getCategoryId() != null) p.setCategoryId(updatedData.getCategoryId());
            return productRepository.save(p);
        }).orElse(null);
    }

    // Xóa mềm
    public boolean softDeleteProduct(Long id) {
        return productRepository.findById(id).map(p -> {
            p.setIsDeleted(true);
            productRepository.save(p);
            return true;
        }).orElse(false);
    }

    // Khôi phục sản phẩm đã bị xóa
    public boolean restoreProduct(Long id) {
        return productRepository.findById(id).map(p -> {
            p.setIsDeleted(false);
            productRepository.save(p);
            return true;
        }).orElse(false);
    }
}
