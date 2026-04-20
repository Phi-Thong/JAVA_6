package asm.poly.asm_java6.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private Long conversationId;   // ID cuộc trò chuyện
    private Long senderId;         // ID người gửi (BẮT BUỘC PHẢI CÓ)
    private String senderName;     // Tên người gửi
    private String senderAvatar;   // Ảnh đại diện người gửi
    private String content;        // Nội dung tin nhắn
    private String timestamp;      // Thời gian gửi (String hoặc LocalDateTime)
    private Boolean isRead;        // Đã đọc hay chưa (nếu cần)
}