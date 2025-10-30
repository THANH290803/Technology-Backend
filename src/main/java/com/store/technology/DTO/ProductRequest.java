package com.store.technology.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductRequest {
    private String name;
    private Integer totalQuality;
    private Long brandId;
    private Long categoryId;
    private List<ProductDetailRequest> productDetails;
}
