package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.enity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    // Lấy tất cả đánh giá của 1 sản phẩm, mới nhất trước
    List<ProductReview> findByProductIdOrderByCreatedAtDesc(Long productId);
}