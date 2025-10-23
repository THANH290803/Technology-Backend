package com.store.technology.Service;

import com.store.technology.Entity.Product;
import com.store.technology.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAllActive();
    }

    public Optional<Product> getById(Long id) {
        return productRepository.findActiveById(id);
    }

    public Product create(Product product) {

        return productRepository.save(product);
    }

    public Product update(Long id, Product productDetails) {
        Optional<Product> productOpt = productRepository.findActiveById(id);
        if (productOpt.isEmpty()) return null;

        Product product = productOpt.get();
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setBrand(productDetails.getBrand());
        product.setCategory(productDetails.getCategory());

        return productRepository.save(product);
    }

    public boolean softDelete(Long id) {
        Optional<Product> productOpt = productRepository.findActiveById(id);
        if (productOpt.isEmpty()) return false;

        Product product = productOpt.get();
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
        return true;
    }

    public boolean restore(Long id) {
        Optional<Product> productOpt = productRepository.findByIdIncludingDeleted(id);
        if (productOpt.isEmpty()) return false;

        Product product = productOpt.get();
        product.setDeletedAt(null);
        productRepository.save(product);
        return true;
    }
}
