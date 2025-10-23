package com.store.technology.Controller;

import com.store.technology.Entity.ProductDetail;
import com.store.technology.Service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-details")
public class ProductDetailController {

    @Autowired
    private ProductDetailService productDetailService;

    @GetMapping
    public List<ProductDetail> getAll() {
        return productDetailService.getAll();
    }

    @GetMapping("/{id}")
    public Optional<ProductDetail> getById(@PathVariable Long id) {
        return productDetailService.getById(id);
    }

    @PostMapping
    public ProductDetail create(@RequestBody ProductDetail detail) {
        return productDetailService.create(detail);
    }

    @PutMapping("/{id}")
    public ProductDetail update(@PathVariable Long id, @RequestBody ProductDetail detail) {
        return productDetailService.update(id, detail);
    }

    @DeleteMapping("/{id}")
    public boolean softDelete(@PathVariable Long id) {
        return productDetailService.softDelete(id);
    }

    @PutMapping("/restore/{id}")
    public boolean restore(@PathVariable Long id) {
        return productDetailService.restore(id);
    }
}
