package asm.poly.asm_java6.service;

import asm.poly.asm_java6.enity.Cart_item;

import java.util.List;

public interface CartItemService {
    List<Cart_item> findByCartId(Long cartId);

    Cart_item save(Cart_item item);

    void deleteById(Long id);
    // Thêm các phương thức khác nếu cần
}