package asm.poly.asm_java6.service;

import asm.poly.asm_java6.enity.ProductReview;

import java.util.List;

public interface ProductReviewService {
    List<ProductReview> getReviewsByProductId(Long productId);
}