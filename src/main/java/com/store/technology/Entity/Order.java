package com.store.technology.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "status", nullable = false)
    private Integer status;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "order_code", nullable = false, unique = true, length = 12)
    private String orderCode; // mã đơn hàng 12 ký tự

    @Column(name = "customer_name", nullable = true, length = 255)
    private String customerName;

    @Column(name = "customer_phone", nullable = true, length = 10)
    private String customerPhone;

    @Column(name = "customer_address", nullable = true, length = 255)
    private String customerAddress;

    @Column(name = "total_price")
    private Integer totalPrice; // tổng tiền đã bao gồm VAT

    @Column(name = "vat")
    private Integer vat; // VAT riêng

    @Column(name = "note", length = 500, nullable = true)
    private String note; // ghi chú thêm của khách

    @Column(name = "payment_method", nullable = true)
    private Integer paymentMethod;

    @Column(name = "user_id")
    @JoinColumn(name = "user_id", nullable = true)
    private Long userId; // khoá ngoại liên kết với bảng users

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

    @Column(name = "deleted_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime deletedAt;
}
