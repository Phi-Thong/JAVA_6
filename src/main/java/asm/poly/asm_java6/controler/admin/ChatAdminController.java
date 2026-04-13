package asm.poly.asm_java6.controler.admin;

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
public class ChatAdminController {

    @MessageMapping("/admin.sendMessage")
    @SendTo("/topic/admin")
    public ChatMessage sendAdminMessage(@Payload ChatMessage chatMessage) {
        // KHÔNG lấy tên từ SecurityContext nữa!
        // Chỉ relay lại message client gửi lên

        // Set timestamp nếu chưa có
        if (chatMessage.getTimestamp() == null || chatMessage.getTimestamp().isEmpty()) {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            chatMessage.setTimestamp(now);
        }

        // TODO: Nếu muốn lưu vào DB, gọi service ở đây

        return chatMessage;
    }
}