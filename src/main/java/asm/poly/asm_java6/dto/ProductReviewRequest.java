
package asm.poly.asm_java6.dto;

import lombok.Data;

@Data
public class ProductReviewRequest {
    private Long productId;
    private int rating;
    private String comment;
}