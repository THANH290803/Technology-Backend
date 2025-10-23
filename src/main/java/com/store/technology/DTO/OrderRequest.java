package com.store.technology.DTO;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrderRequest {
    private Long userId;
    private List<Long> productIds;
    private String address;
}
