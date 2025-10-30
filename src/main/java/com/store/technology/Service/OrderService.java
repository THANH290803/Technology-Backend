package com.store.technology.Service;

import com.store.technology.DTO.OrderDetailRequest;
import com.store.technology.DTO.OrderRequest;
import com.store.technology.DTO.ProductDetailRequest;
import com.store.technology.Entity.*;
import com.store.technology.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductDetailRepository productDetailRepository;
    private final TelegramService telegramService;

    public OrderService(OrderRepository orderRepository,
                        OrderDetailRepository orderDetailRepository,
                        ProductDetailRepository productDetailRepository,
                        TelegramService telegramService) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productDetailRepository = productDetailRepository;
        this.telegramService = telegramService;
    }

    public List<Order> getAllOrders(boolean includeDeleted) {
        return includeDeleted ? orderRepository.findAllIncludingDeleted() : orderRepository.findAllNotDeleted();
    }

    public List<Order> getDeletedOrders() {
        return orderRepository.findAllDeleted();
    }

    public Optional<Order> getOrderById(Long id, boolean includeDeleted) {
        if (includeDeleted) {
            return Optional.ofNullable(orderRepository.findAnyById(id));
        } else {
            return Optional.ofNullable(orderRepository.findNotDeletedById(id));
        }
    }

    // 🔹 Tạo đơn hàng mới + gửi tin nhắn Telegram
    @Transactional
    public Order createOrderFromRequest(OrderRequest request) {
        // 🔹 Bước 1: Tạo và lưu đơn hàng
        Order order = new Order();
        order.setStatus(request.getStatus());
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setUserId(request.getUserId());

        // 🔹 Set ngày tạo
        order.setCreatedDate(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // 🔹 Bước 2: Lưu chi tiết đơn hàng
        List<OrderDetailRequest> orderDetailsRequest = request.getOrderDetails();
        if (orderDetailsRequest != null && !orderDetailsRequest.isEmpty()) {
            for (OrderDetailRequest detailReq : orderDetailsRequest) {
                ProductDetailRequest pdReq = detailReq.getProductDetail();

                ProductDetail productDetail = productDetailRepository
                        .findByProduct_IdAndConfiguration_Id(
                                pdReq.getProductId(),
                                pdReq.getConfigurationId()
                        ).orElseThrow(() -> new RuntimeException(
                                "Không tìm thấy chi tiết sản phẩm với productId=" +
                                        pdReq.getProductId() + " và configurationId=" + pdReq.getConfigurationId()
                        ));

                OrderDetail detail = new OrderDetail();
                detail.setOrder(savedOrder);
                detail.setProductDetail(productDetail);
                detail.setQuantity(detailReq.getQuantity());
                detail.setUnitPrice(detailReq.getUnitPrice());

                orderDetailRepository.save(detail);
            }
        }

        // 🔹 Bước 3: Tính tổng tiền và tạo danh sách sản phẩm
        double totalAmount = 0;
        StringBuilder productList = new StringBuilder();
        for (OrderDetailRequest detailReq : orderDetailsRequest) {
            ProductDetailRequest pdReq = detailReq.getProductDetail();

            ProductDetail productDetail = productDetailRepository
                    .findByProduct_IdAndConfiguration_Id(pdReq.getProductId(), pdReq.getConfigurationId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm"));

            int quantity = detailReq.getQuantity();
            double unitPrice = detailReq.getUnitPrice();
            double lineTotal = quantity * unitPrice;
            totalAmount += lineTotal;

            String productName = productDetail.getProduct().getName();
            String configurationName = productDetail.getConfiguration().getName();

            productList.append(String.format(
                    "• %s\n  ⚙️ Cấu hình: %s\n  SL: %d x %,.0f ₫ = %,.0f ₫\n",
                    productName, configurationName, quantity, unitPrice, lineTotal
            ));
        }

        // 🔹 Bước 4: Xác định phương thức thanh toán
        String paymentMethodText = switch (savedOrder.getPaymentMethod()) {
            case 1 -> "Thanh toán khi nhận hàng";
            case 2 -> "Thanh toán online";
            default -> "Không xác định";
        };

        // 🔹 Bước 5: Định dạng tiền Việt Nam
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

        // 🔹 Bước 6: Soạn tin nhắn Telegram
        String message = """
            🛎️ <b>THÔNG BÁO: ĐƠN HÀNG MỚI</b>
            ──────────────────────────────────
            👤 <b>Khách hàng:</b> %s
            📞 <b>SĐT:</b> %s
            📍 <b>Địa chỉ:</b> %s

            💳 <b>Phương thức thanh toán:</b> %s

            📦 <b>Sản phẩm:</b>
            <pre>
            %s
            </pre>
            ──────────────────────────────────
            💰 <b>Tổng tiền:</b> %s ₫
            """.formatted(
                savedOrder.getCustomerName(),
                savedOrder.getCustomerPhone(),
                savedOrder.getCustomerAddress(),
                paymentMethodText,
                productList,
                currencyFormat.format(totalAmount)
        );

        // 🔹 Bước 7: Gửi tin nhắn Telegram
        telegramService.sendOrderMessage(message);

        return savedOrder;
    }

    public Order patchUpdateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findAnyById(id);
        if (order == null) return null;

        if (orderDetails.getStatus() != null) order.setStatus(orderDetails.getStatus());
        if (orderDetails.getCustomerName() != null) order.setCustomerName(orderDetails.getCustomerName());
        if (orderDetails.getCustomerPhone() != null) order.setCustomerPhone(orderDetails.getCustomerPhone());
        if (orderDetails.getCustomerAddress() != null) order.setCustomerAddress(orderDetails.getCustomerAddress());
        if (orderDetails.getPaymentMethod() != null) order.setPaymentMethod(orderDetails.getPaymentMethod());
        if (orderDetails.getUserId() != null) order.setUserId(orderDetails.getUserId());

        return orderRepository.save(order);
    }

    public boolean softDeleteOrder(Long id) {
        Order order = orderRepository.findNotDeletedById(id);
        if (order == null) return false;
        order.setDeletedAt(LocalDateTime.now());
        orderRepository.save(order);
        return true;
    }

    public boolean restoreOrder(Long id) {
        Order order = orderRepository.findAnyById(id);
        if (order == null || order.getDeletedAt() == null) return false;
        order.setDeletedAt(null);
        orderRepository.save(order);
        return true;
    }
}

