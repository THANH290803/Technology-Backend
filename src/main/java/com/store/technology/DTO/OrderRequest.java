package com.store.technology.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {
    private Integer status;
    private LocalDateTime createdDate;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private Integer paymentMethod;
    private Long userId;
    private List<OrderDetailRequest> orderDetails;
}
