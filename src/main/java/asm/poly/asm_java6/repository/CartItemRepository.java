package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.enity.Cart_item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<Cart_item, Long> {
    List<Cart_item> findByCartId(Long cartId);
}