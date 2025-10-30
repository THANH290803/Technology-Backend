package com.store.technology.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "product_code", unique = true, nullable = false, length = 6)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String productCode;

    @Column(nullable = false)
    private String name;

    @Column(name = "total_quality", nullable = true)
    private Integer totalQuality;

    // ✅ Quan hệ với Brand
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Brand brand;

    // ✅ Quan hệ với Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;

    @Column(name = "is_deleted")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @PrePersist
    public void generateProductCode() {
        if (this.productCode == null) {
            this.productCode = generateRandomCode();
        }
    }

    private String generateRandomCode() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6).toUpperCase();
        return uuid;
    }
}
