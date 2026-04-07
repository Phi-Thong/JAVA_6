package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.enity.Cart;
import asm.poly.asm_java6.enity.users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(users user);
}