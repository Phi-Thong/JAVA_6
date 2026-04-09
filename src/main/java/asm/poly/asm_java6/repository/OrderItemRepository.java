package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.enity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}