package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.enity.Cart_item;
import org.springframework.data.jpa.repository.JpaRepository;
import asm.poly.asm_java6.enity.users;

import java.util.List;

public interface CartItemRepository extends JpaRepository<Cart_item, Long> {
    List<Cart_item> findByCartId(Long cartId);

    void deleteByUser(users user);
}