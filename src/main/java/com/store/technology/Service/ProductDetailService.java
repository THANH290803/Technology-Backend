package com.store.technology.Service;

import com.store.technology.Entity.Configuration;
import com.store.technology.Entity.Product;
import com.store.technology.Entity.ProductDetail;
import com.store.technology.Repository.ConfigurationRepository;
import com.store.technology.Repository.ProductDetailRepository;
import com.store.technology.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductDetailService {
    private final ProductDetailRepository repository;
    private final ProductRepository productRepository;
    private final ConfigurationRepository configurationRepository;

    public ProductDetailService(ProductDetailRepository repository,
                                ProductRepository productRepository,
                                ConfigurationRepository configurationRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.configurationRepository = configurationRepository;
    }

    public List<ProductDetail> getAllNotDeleted() {
        return repository.findAllNotDeleted();
    }

    public List<ProductDetail> getAllIncludingDeleted() {
        return repository.findAllIncludingDeleted();
    }

    public ProductDetail getById(Long id, boolean includeDeleted) {
        return includeDeleted ? repository.findAnyById(id) : repository.findNotDeletedById(id);
    }

    public ProductDetail save(ProductDetail detail) {
        return repository.save(detail);
    }

    public ProductDetail update(Long id, ProductDetail updated) {
        ProductDetail existing = repository.findAnyById(id);
        if (existing == null) return null;

        existing.setQuantity(updated.getQuantity());
        existing.setPrice(updated.getPrice());
        existing.setConfiguration(updated.getConfiguration());
        existing.setProduct(updated.getProduct());
        return repository.save(existing);
    }

    public ProductDetail createFromJson(Long configId, Long productId, Integer quantity, Integer price) {
        Configuration config = configurationRepository.findById(configId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        if (config == null || product == null) return null;

        ProductDetail detail = new ProductDetail();
        detail.setConfiguration(config);
        detail.setProduct(product);
        detail.setQuantity(quantity);
        detail.setPrice(price);
        return repository.save(detail);
    }

    public ProductDetail updateFromJson(Long id, Long configId, Long productId, Integer quantity, Integer price) {
        ProductDetail existing = repository.findAnyById(id);
        if (existing == null) return null;

        Configuration config = configurationRepository.findById(configId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        if (config != null) existing.setConfiguration(config);
        if (product != null) existing.setProduct(product);
        existing.setQuantity(quantity);
        existing.setPrice(price);
        return repository.save(existing);
    }

    public void softDelete(Long id) {
        ProductDetail existing = repository.findNotDeletedById(id);
        if (existing != null) {
            existing.setDeletedAt(LocalDateTime.now());
            repository.save(existing);
        }
    }

    public void restore(Long id) {
        ProductDetail existing = repository.findAnyById(id);
        if (existing != null && existing.getDeletedAt() != null) {
            existing.setDeletedAt(null);
            repository.save(existing);
        }
    }
}
