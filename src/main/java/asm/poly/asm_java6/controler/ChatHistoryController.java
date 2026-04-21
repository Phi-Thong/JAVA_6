package asm.poly.asm_java6.controler;

import asm.poly.asm_java6.enity.Message;
import asm.poly.asm_java6.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

    @Autowired
    private MessageRepository messageRepo;

    @GetMapping("/history/{conversationId}")
    public List<Message> getHistory(@PathVariable Long conversationId) {
        return messageRepo.findByConversationIdOrderBySentAtAsc(conversationId);
    }
}