package asm.poly.asm_java6.enity;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
import lombok.Builder;


@Entity
@Table(name = "cart_item")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Cart_item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Khóa ngoại tới bảng users
    @ManyToOne
    @JoinColumn(name = "user_id")
    private users user;

    // Khóa ngoại tới bảng cart
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    private Cart cart;

    // Khóa ngoại tới bảng product
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;


}