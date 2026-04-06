package asm.poly.asm_java6.service.impl;

import asm.poly.asm_java6.enity.ProductReview;
import asm.poly.asm_java6.repository.ProductReviewRepository;
import asm.poly.asm_java6.service.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public class ProductReviewServiceImpl implements ProductReviewService {
    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Override
    public List<ProductReview> getReviewsByProductId(Long productId) {
        return productReviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }
}
