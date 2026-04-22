package asm.poly.asm_java6.controler.user;

import asm.poly.asm_java6.enity.Conversation;
import asm.poly.asm_java6.enity.Message;
import asm.poly.asm_java6.repository.ConversationRepository;
import asm.poly.asm_java6.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ConversationRepository conversationRepo;

    @Autowired
    private MessageRepository messageRepo;

    // Lấy danh sách cuộc trò chuyện của user
    @GetMapping("/conversations/{userId}")
    public List<Conversation> getConversations(@PathVariable Long userId) {
        return conversationRepo.findByUserId(userId);
    }

    // Lấy tin nhắn của 1 cuộc trò chuyện
    @GetMapping("/messages/{conversationId}")
    public List<Message> getMessages(@PathVariable Long conversationId) {
        return messageRepo.findByConversationIdOrderBySentAtAsc(conversationId);
    }

    // Gửi tin nhắn mới
    @PostMapping("/messages")
    public Message sendMessage(@RequestBody Message message) {
        message.setSentAt(java.time.LocalDateTime.now());
        return messageRepo.save(message);
    }

    @GetMapping("/conversation/current")
    public Map<String, Long> getOrCreateConversationForCurrentUser(@RequestAttribute("userId") Long userId) {
        Conversation conversation = conversationRepo.findByUserId(userId)
                .stream().findFirst().orElse(null);

        if (conversation == null) {
            conversation = new Conversation();
            conversation.setUserId(userId);
            conversation = conversationRepo.save(conversation);
        }
        return Map.of("conversationId", conversation.getId());
    }
}