package com.store.technology.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailRequest {
//    private Long id;
    private Long configurationId;
    private Long productId;
    private Integer quantity;
    private Integer price;
}
