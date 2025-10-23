package com.store.technology.Controller;

import com.store.technology.Entity.Cart;
import com.store.technology.Service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Cart", description = "CRUD API cho giỏ hàng (Soft Delete + Restore)")
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "Lấy danh sách giỏ hàng chưa xoá")
    public ResponseEntity<List<Cart>> getAll() {
        return ResponseEntity.ok(cartService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy giỏ hàng theo ID")
    public ResponseEntity<Cart> getById(@PathVariable Long id) {
        Optional<Cart> opt = cartService.getById(id);
        return opt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Tạo giỏ hàng mới")
    public ResponseEntity<Cart> create(@RequestBody Cart cart) {
        return ResponseEntity.ok(cartService.create(cart));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Cập nhật giỏ hàng")
    public ResponseEntity<Cart> update(@PathVariable Long id, @RequestBody Cart data) {
        Cart updated = cartService.update(id, data);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xoá mềm giỏ hàng")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean result = cartService.softDelete(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/restore")
    @Operation(summary = "Khôi phục giỏ hàng đã xoá")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        boolean result = cartService.restore(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
