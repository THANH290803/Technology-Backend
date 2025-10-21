package com.store.technology.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "is_deleted")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted = false;

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
