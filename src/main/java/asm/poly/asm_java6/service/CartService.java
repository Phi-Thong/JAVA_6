package asm.poly.asm_java6.service;

import asm.poly.asm_java6.enity.Cart;
import asm.poly.asm_java6.enity.users;

public interface CartService {
    Cart getCartByUser(users user);

    Cart addToCart(users user, Long productId, int quantity);
}