package asm.poly.asm_java6.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private Long conversationId;   // ID của cuộc trò chuyện
    private Long senderId;         // ID người gửi
    private String senderName;     // Tên người gửi
    private String senderAvatar;   // Ảnh đại diện người gửi (nếu có)
    private String content;        // Nội dung tin nhắn
    private Boolean isRead;        // Đã đọc hay chưa
    private Integer vai_tro;       // 1 = nhân viên, 0 = user
    private LocalDateTime sentAt;  // Thời gian gửi
}