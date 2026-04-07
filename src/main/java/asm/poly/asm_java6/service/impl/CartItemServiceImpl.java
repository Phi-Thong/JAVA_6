package asm.poly.asm_java6.service.impl;

import asm.poly.asm_java6.enity.Cart_item;
import asm.poly.asm_java6.repository.CartItemRepository;
import asm.poly.asm_java6.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public List<Cart_item> findByCartId(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public Cart_item save(Cart_item item) {
        return cartItemRepository.save(item);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}