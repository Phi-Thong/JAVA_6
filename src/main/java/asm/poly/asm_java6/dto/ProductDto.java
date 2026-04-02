package asm.poly.asm_java6.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String tenSanPham;
    private BigDecimal gia;
    private String moTa;
    private Long categoryId;
    private String anhChinh;
    private String anhPhu1;
    private String anhPhu2;
    private String anhPhu3;
    private String anhPhu4;
    private LocalDateTime createdAt;
    private Object brand;
    private List<ProductSizeDto> productSizes;
}