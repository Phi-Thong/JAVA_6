package asm.poly.asm_java6.controler;

import asm.poly.asm_java6.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        // KHÔNG lấy tên từ SecurityContext nữa!
        // Chỉ relay lại message client gửi lên

        // Nếu client không gửi timestamp thì set ở backend
        if (chatMessage.getTimestamp() == null || chatMessage.getTimestamp().isEmpty()) {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            chatMessage.setTimestamp(now);
        }

        // Nếu muốn lưu vào SQL, gọi service lưu ở đây

        return chatMessage;
    }
}