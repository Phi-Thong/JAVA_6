package asm.poly.asm_java6.service.impl;

import asm.poly.asm_java6.enity.Cart;
import asm.poly.asm_java6.enity.Cart_item;
import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.CartRepository;
import asm.poly.asm_java6.repository.ProductRepository;
import asm.poly.asm_java6.service.CartService;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public Cart getCartByUser(users user) {
        return cartRepository.findByUser(user);
    }

    @Override
    public Cart addToCart(users user, Long productId, int quantity) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
        }

        Optional<Cart_item> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            Cart_item item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            Product product = productRepository.findById(productId).orElseThrow();
            Cart_item newItem = new Cart_item();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }
}