package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.enity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}