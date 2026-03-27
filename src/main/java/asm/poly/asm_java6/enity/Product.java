package asm.poly.asm_java6.enity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_san_pham", nullable = false)
    private String tenSanPham;

    @Column(name = "gia", nullable = false)
    private BigDecimal gia;

    @Column(name = "mo_ta")
    private String moTa;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "anh_chinh")
    private String anhChinh;

    @Column(name = "anh_phu_1")
    private String anhPhu1;

    @Column(name = "anh_phu_2")
    private String anhPhu2;

    @Column(name = "anh_phu_3")
    private String anhPhu3;

    @Column(name = "anh_phu_4")
    private String anhPhu4;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
