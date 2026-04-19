package asm.poly.asm_java6.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderSummaryDTO {

    private Long id;
    private String customerName;
    private String customerEmail;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private int itemCount;
    private String status;

    public OrderSummaryDTO(
            Long id,
            String customerName,
            String customerEmail,
            LocalDateTime orderDate,
            BigDecimal totalAmount,
            int itemCount,
            String status
    ) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.itemCount = itemCount;
        this.status = status;
    }
}