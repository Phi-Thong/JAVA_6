package asm.poly.asm_java6.enity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết tới sản phẩm
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Liên kết tới user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private users user;

    @Column(nullable = false)
    private int rating; // số sao

    @Column(name = "comment", columnDefinition = "NVARCHAR(1000)")
    private String comment; // nội dung bình luận

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}