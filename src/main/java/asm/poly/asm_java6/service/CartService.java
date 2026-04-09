package asm.poly.asm_java6.service;

import asm.poly.asm_java6.enity.Cart;
import asm.poly.asm_java6.enity.Cart_item;
import asm.poly.asm_java6.enity.users;

import java.util.List;

public interface CartService {
    Cart getCartByUser(users user);

    Cart addToCart(users user, Long productId, int quantity);

    Cart updateCartItem(users user, Long productId, Integer quantity);

    Cart removeCartItem(users user, Long productId);

    List<Cart_item> getCartItemsByUser(users user);

    void clearCart(users user);


    Cart removeFromCart(users user, Long productId);
}