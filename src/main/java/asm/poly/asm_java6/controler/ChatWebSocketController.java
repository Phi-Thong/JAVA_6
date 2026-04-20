package asm.poly.asm_java6.controler;

import asm.poly.asm_java6.dto.MessageDTO;
import asm.poly.asm_java6.enity.Message;
import asm.poly.asm_java6.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageRepository messageRepo;

    // Nhận message từ client gửi lên
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageDTO messageDTO) {
        Message message = new Message();
        message.setConversationId(messageDTO.getConversationId());
        message.setSenderId(messageDTO.getSenderId());
        message.setSenderName(messageDTO.getSenderName());
        message.setSenderAvatar(messageDTO.getSenderAvatar());
        message.setContent(messageDTO.getContent());
      
        message.setSentAt(java.time.LocalDateTime.now());
        message.setIsRead(false); // hoặc true tùy logic

        messageRepo.save(message);

        // Gửi message tới tất cả client đang subscribe topic này
        messagingTemplate.convertAndSend("/topic/conversation." + message.getConversationId(), message);
    }
}