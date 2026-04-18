
package asm.poly.asm_java6.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductReviewResponse {
    private Long id;              // ID đánh giá
    private Long productId;       // ID sản phẩm
    private Long userId;          // ID người dùng
    private String userName;      // Tên người dùng
    private String userAvatar;    // Ảnh đại diện người dùng (URL)
    private int rating;           // Số sao
    private String comment;       // Nội dung bình luận
    private LocalDateTime createdAt; // Thời gian đánh giá
}