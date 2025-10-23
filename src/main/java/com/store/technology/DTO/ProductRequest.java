package com.store.technology.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Long categoryId;
    private Long brandId;
}
