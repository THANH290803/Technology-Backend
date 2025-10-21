package com.store.technology.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "product_details")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    // 🔗 Quan hệ ManyToOne với Configuration
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "configuration_id", nullable = false)
    private Configuration configuration;

    // 🔗 Quan hệ ManyToOne với Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 15, scale = 2)
    private Integer price;

    @Column(name = "deleted_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime deletedAt; // null = chưa xoá, khác null = đã xoá
}
