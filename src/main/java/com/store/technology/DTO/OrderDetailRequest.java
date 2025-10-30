package com.store.technology.DTO;

import com.store.technology.Entity.ProductDetail;
import lombok.Data;

@Data
public class OrderDetailRequest {
    private Integer quantity;
    private Integer unitPrice;
    private ProductDetailRequest productDetail;
}
