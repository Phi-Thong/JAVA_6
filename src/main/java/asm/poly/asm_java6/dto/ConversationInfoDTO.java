package asm.poly.asm_java6.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConversationInfoDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerAvatar;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
}