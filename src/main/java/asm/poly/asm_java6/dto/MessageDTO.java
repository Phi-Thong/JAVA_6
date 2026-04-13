package asm.poly.asm_java6.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private Long senderId;
    private String content;
    private Integer vai_tro; // 1 = nhân viên, 0 = user
    private LocalDateTime createdAt;
}