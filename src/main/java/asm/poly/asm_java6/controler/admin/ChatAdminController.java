package asm.poly.asm_java6.controler.admin;

import asm.poly.asm_java6.dto.ChatMessage;
import asm.poly.asm_java6.enity.Message;
import asm.poly.asm_java6.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MessageRepository messageRepo;

    @MessageMapping("/admin.sendMessage")
    @SendTo("/topic/admin")
    public ChatMessage sendAdminMessage(@Payload ChatMessage chatMessage) {
        // Set timestamp nếu chưa có
        if (chatMessage.getTimestamp() == null || chatMessage.getTimestamp().isEmpty()) {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            chatMessage.setTimestamp(now);
        }

        // Tạo entity Message và lưu vào DB
        Message message = new Message();
        message.setConversationId(chatMessage.getConversationId());
        message.setSenderId(chatMessage.getSenderId());
        message.setSenderName(chatMessage.getSenderName());
        message.setSenderAvatar(chatMessage.getSenderAvatar());
        message.setContent(chatMessage.getContent());
        message.setSentAt(LocalDateTime.now());
        message.setIsRead(false);

        messageRepo.save(message);

        return chatMessage;
    }
}