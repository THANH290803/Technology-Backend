package com.store.technology.Service;

import com.store.technology.Entity.Cart;
import com.store.technology.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    // Lấy tất cả cart chưa bị xóa (vì @Where tự lọc deleted_at IS NULL)
    public List<Cart> getAll() {
        return cartRepository.findAll();
    }

    // Lấy cart theo id (tự động bỏ qua cart đã bị soft delete)
    public Optional<Cart> getById(Long id) {
        return cartRepository.findById(id);
    }

    // Tạo mới cart
    public Cart create(Cart cart) {
        cart.setCreatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    // Cập nhật cart
    public Cart update(Long id, Cart data) {
        Optional<Cart> cartOpt = cartRepository.findById(id);
        if (cartOpt.isEmpty()) return null;

        Cart cart = cartOpt.get();
        cart.setUser(data.getUser());
        cart.setUpdatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    // Soft delete cart (đánh dấu deleted_at)
    public boolean softDelete(Long id) {
        Optional<Cart> cartOpt = cartRepository.findById(id);
        if (cartOpt.isEmpty()) return false;

        Cart cart = cartOpt.get();
        cart.setDeletedAt(LocalDateTime.now());
        cartRepository.save(cart);
        return true;
    }

    // Khôi phục cart đã bị soft delete
    public boolean restore(Long id) {
        Optional<Cart> cartOpt = cartRepository.findByIdIncludingDeleted(id);
        if (cartOpt.isEmpty()) return false;

        Cart cart = cartOpt.get();
        cart.setDeletedAt(null);
        cartRepository.save(cart);
        return true;
    }
}
