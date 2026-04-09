package asm.poly.asm_java6.enity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import asm.poly.asm_java6.enity.Order;
import lombok.*;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Đơn hàng cha
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // Sản phẩm
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Số lượng mua
    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    // Giá tại thời điểm đặt hàng
    @Column(name = "gia", nullable = false)
    private BigDecimal gia;
}