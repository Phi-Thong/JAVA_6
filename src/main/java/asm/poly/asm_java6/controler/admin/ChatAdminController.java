package asm.poly.asm_java6.controler.admin;

import asm.poly.asm_java6.dto.ChatMessage;
import asm.poly.asm_java6.dto.ConversationInfoDTO;
import asm.poly.asm_java6.enity.Conversation;
import asm.poly.asm_java6.enity.Message;
import asm.poly.asm_java6.enity.users;
import asm.poly.asm_java6.repository.ConversationRepository;
import asm.poly.asm_java6.repository.MessageRepository;
import asm.poly.asm_java6.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatAdminController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private ConversationRepository conversationRepo;
    @Autowired
    private UsersRepository usersRepository;

    @MessageMapping("/admin.sendMessage")
    public void sendAdminMessage(@Payload ChatMessage chatMessage) {

        if (chatMessage.getTimestamp() == null || chatMessage.getTimestamp().isEmpty()) {
            String now = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            chatMessage.setTimestamp(now);
        }

        Message message = new Message();
        message.setConversationId(chatMessage.getConversationId());
        message.setSenderId(chatMessage.getSenderId());
        message.setSenderName(chatMessage.getSenderName());
        message.setSenderAvatar(chatMessage.getSenderAvatar());
        message.setContent(chatMessage.getContent());
        message.setSentAt(LocalDateTime.now());
        message.setIsRead(false);

        messageRepo.save(message);


        messagingTemplate.convertAndSend(
                "/topic/conversation." + message.getConversationId(),
                message
        );
    }

    @GetMapping("/api/chat/conversations")
    @ResponseBody
    public List<ConversationInfoDTO> getAllConversations() {
        List<Conversation> conversations = conversationRepo.findAll();
        List<ConversationInfoDTO> result = new ArrayList<>();
        for (Conversation c : conversations) {
            // Lấy thông tin user từ userId
            users user = usersRepository.findById(c.getUserId()).orElse(null);
            Long customerId = c.getUserId();
            String customerName = user != null ? user.getHoTen() : "Khách";
            String customerAvatar = user != null ? user.getAvatar() : null;

            // Lấy tin nhắn cuối cùng
            Message lastMsg = messageRepo.findTopByConversationIdOrderBySentAtDesc(c.getId());
            String lastMessage = lastMsg != null ? lastMsg.getContent() : "";
            LocalDateTime lastMessageTime = lastMsg != null ? lastMsg.getSentAt() : null;

            ConversationInfoDTO dto = new ConversationInfoDTO();
            dto.setId(c.getId());
            dto.setCustomerId(customerId);
            dto.setCustomerName(customerName);
            dto.setCustomerAvatar(customerAvatar);
            dto.setLastMessage(lastMessage);
            dto.setLastMessageTime(lastMessageTime);

            result.add(dto);
        }
        return result;
    }
}