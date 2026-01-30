package com.store.technology.Service;

import com.store.technology.DTO.OrderDetailRequest;
import com.store.technology.DTO.OrderRequest;
import com.store.technology.DTO.ProductDetailRequest;
import com.store.technology.Entity.*;
import com.store.technology.Repository.*;
import jakarta.transaction.Transactional;
import org.apache.commons.text.StringEscapeUtils;
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

    private String generateOrderCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int idx = (int) (Math.random() * chars.length());
            code.append(chars.charAt(idx));
        }
        return code.toString();
    }

    public List<Order> getAllOrders(boolean includeDeleted) {
        return includeDeleted ? orderRepository.findAllIncludingDeleted() : orderRepository.findAllNotDeleted();
    }

    public List<Order> getDeletedOrders() {
        return orderRepository.findAllDeleted();
    }

    // Lấy đơn hàng theo user
    public List<Order> getOrdersByUser(Long userId) {
        if (userId == null || userId == 0) {
            throw new RuntimeException("UserId không hợp lệ");
        }
        return orderRepository.findByUserIdNotDeleted(userId);
    }

    public Optional<Order> getOrderById(Long id, boolean includeDeleted) {
        Order order = includeDeleted
                ? orderRepository.findAnyById(id)
                : orderRepository.findNotDeletedById(id);

        return Optional.ofNullable(order); // trả về Optional để tránh NullPointerException
    }

    @Transactional
    public Order createOrderFromRequest(OrderRequest request) {
        // 🔹 Bước 1: Tạo đơn hàng
        Order order = new Order();
        order.setStatus(request.getStatus());
        order.setOrderCode(generateOrderCode());
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setNote(request.getNote());
        order.setVat(request.getVat() != null ? request.getVat() : 0);
        order.setTotalPrice(request.getTotalPrice() != null ? request.getTotalPrice() : 0);

        // Xử lý userId an toàn
        if (request.getUserId() != null && request.getUserId() != 0) {
            order.setUserId(request.getUserId());
        } else {
            order.setUserId(null);
        }

        order.setCreatedDate(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        // 🔹 Bước 2: Lưu chi tiết đơn hàng
        List<OrderDetailRequest> orderDetailsRequest = request.getOrderDetails();
        StringBuilder productList = new StringBuilder();
        double calculatedTotal = 0;

        if (orderDetailsRequest != null && !orderDetailsRequest.isEmpty()) {
            for (OrderDetailRequest detailReq : orderDetailsRequest) {
                ProductDetailRequest pdReq = detailReq.getProductDetail();

                ProductDetail productDetail = productDetailRepository
                        .findByProduct_IdAndConfiguration_Id(pdReq.getProductId(), pdReq.getConfigurationId())
                        .orElseThrow(() -> new RuntimeException(
                                "Không tìm thấy chi tiết sản phẩm với productId=" +
                                        pdReq.getProductId() + " và configurationId=" + pdReq.getConfigurationId()
                        ));

                // Lưu chi tiết đơn
                OrderDetail detail = new OrderDetail();
                detail.setOrder(savedOrder);
                detail.setProductDetail(productDetail);
                detail.setQuantity(detailReq.getQuantity());
                detail.setUnitPrice(detailReq.getUnitPrice());
                orderDetailRepository.save(detail);

                // Tính tổng tiền và tạo danh sách sản phẩm
                double lineTotal = detailReq.getQuantity() * detailReq.getUnitPrice();
                calculatedTotal += lineTotal;

                productList.append(String.format(
                        "• %s\n  ⚙️ Cấu hình: %s\n  SL: %d x %,d ₫ = %,d ₫\n",
                        productDetail.getProduct().getName(),
                        productDetail.getConfiguration().getName(),
                        detailReq.getQuantity(),
                        Math.round(detailReq.getUnitPrice()),
                        Math.round(lineTotal)
                ));
            }
        }

        // 🔹 Bước 3: Cập nhật tổng tiền chính xác (nếu muốn)
        // Cập nhật tổng tiền chính xác
        savedOrder.setTotalPrice((int) Math.round(calculatedTotal + savedOrder.getVat()));
        orderRepository.save(savedOrder);

        // 🔹 Bước 4: Xác định phương thức thanh toán
        String paymentMethodText = switch (savedOrder.getPaymentMethod()) {
            case 1 -> "Thanh toán khi nhận hàng";
            case 2 -> "Thanh toán online";
            default -> "Không xác định";
        };

        // 🔹 Bước 5: Format tiền Việt Nam an toàn
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String vatText = currencyFormat.format(savedOrder.getVat());
        String totalText = currencyFormat.format(savedOrder.getTotalPrice());

        // 🔹 Bước 6: Xử lý ghi chú an toàn
        String safeNote = savedOrder.getNote() != null ? savedOrder.getNote() : "Không có";
        if (safeNote.length() > 500) safeNote = safeNote.substring(0, 500) + "...";
        safeNote = StringEscapeUtils.escapeHtml4(safeNote);

        // 🔹 Bước 7: Soạn tin nhắn Telegram
        String message = """
            🛎️ <b>THÔNG BÁO: ĐƠN HÀNG MỚI</b>

            🆔 <b>Mã đơn hàng:</b> %s
            👤 <b>Khách hàng:</b> %s
            📞 <b>SĐT:</b> %s
            📍 <b>Địa chỉ:</b> %s

            💳 <b>Phương thức thanh toán:</b> %s

            📦 <b>Sản phẩm:</b>
            <pre>%s</pre>

            📝 <b>Ghi chú:</b>
            <pre>%s</pre>

            🧾 <b>VAT:</b> %s ₫
            💰 <b>Tổng tiền:</b> %s ₫
            """.formatted(
                savedOrder.getOrderCode(),
                savedOrder.getCustomerName(),
                savedOrder.getCustomerPhone(),
                savedOrder.getCustomerAddress(),
                paymentMethodText,
                productList,
                safeNote,
                vatText,
                totalText
        );

        // 🔹 Bước 8: Gửi tin nhắn Telegram
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
        if (orderDetails.getNote() != null) order.setNote(orderDetails.getNote());
        // Xử lý userId null an toàn
        if (orderDetails.getUserId() != null && orderDetails.getUserId() != 0) {
            order.setUserId(orderDetails.getUserId());
        } else if (orderDetails.getUserId() != null) {
            order.setUserId(null);
        }

        return orderRepository.save(order);
    }

    public Order patchUpdateOrderStatus(Long id, Integer newStatus) {
        Order order = orderRepository.findAnyById(id);
        if (order == null) {
            throw new RuntimeException("Đơn hàng không tồn tại với id = " + id);
        }

        Integer currentStatus = order.getStatus();

        boolean validTransition = switch (currentStatus) {
            case 1 -> (newStatus == 2 || newStatus == 5); // 1 -> 2 hoặc 1 -> 5 (Huỷ)
            case 2 -> (newStatus == 1 || newStatus == 3); // 2 -> 1 hoặc 3
            case 3 -> (newStatus == 4);                   // 3 -> 4
            case 4, 5 -> false;                           // 4 hoặc 5 -> không chuyển
            default -> true;
        };

        if (!validTransition) {
            throw new RuntimeException(
                    "Không thể chuyển trạng thái từ " + statusText(currentStatus) +
                            " sang " + statusText(newStatus)
            );
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    private String statusText(Integer status) {
        return switch (status) {
            case 1 -> "Chờ xử lý";
            case 2 -> "Đã xác nhận";
            case 3 -> "Đang giao hàng";
            case 4 -> "Đã giao";
            case 5 -> "Đã huỷ";
            default -> "Không xác định";
        };
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

