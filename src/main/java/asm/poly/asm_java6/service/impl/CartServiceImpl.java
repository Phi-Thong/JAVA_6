package asm.poly.asm_java6.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import asm.poly.asm_java6.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import asm.poly.asm_java6.enity.Cart;
import asm.poly.asm_java6.enity.Cart_item;
import asm.poly.asm_java6.enity.Product;
import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.CartRepository;
import asm.poly.asm_java6.repository.ProductRepository;
import asm.poly.asm_java6.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

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
            newItem.setUser(user);
            cart.getItems().add(newItem);
        }

        return cartRepository.save(cart);
    }

    @Override
    public Cart updateCartItem(users user, Long productId, Integer quantity) {
        Cart cart = getCartByUser(user);
        if (cart == null) return null;
        cart.getItems().forEach(item -> {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
            }
        });
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeCartItem(users user, Long productId) {
        Cart cart = getCartByUser(user);
        if (cart == null) return null;
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart_item> getCartItemsByUser(users user) {
        Cart cart = getCartByUser(user);
        return cart.getItems(); //
    }

    @Override
    public void clearCart(users user) {
        // Ví dụ nếu bạn dùng JPA:
        cartItemRepository.deleteByUser(user);
    }


    @Override
    public Cart removeFromCart(users user, Long productId) {

        // Lấy cart theo user
        Cart cart = cartRepository.findByUser(user);

        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        // Tìm item cần xóa
        Cart_item itemToRemove = null;

        for (Cart_item item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                itemToRemove = item;
                break;
            }
        }

        // Nếu tìm thấy thì xóa
        if (itemToRemove != null) {
            cart.getItems().remove(itemToRemove);
            cartItemRepository.delete(itemToRemove);
            cartRepository.save(cart);
        }

        return cart;
    }
}