package asm.poly.asm_java6.service.impl;

import asm.poly.asm_java6.enity.Message;
import asm.poly.asm_java6.repository.MessageRepository;
import asm.poly.asm_java6.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message sendMessage(Long conversationId, Long senderId, String senderName, String senderAvatar, String content) {
        Message m = new Message();
        m.setConversationId(conversationId);
        m.setSenderId(senderId);
        m.setSenderName(senderName);
        m.setSenderAvatar(senderAvatar);
        m.setContent(content);
        m.setSentAt(LocalDateTime.now());
        return messageRepository.save(m);
    }

    @Override
    public List<Message> getMessagesByConversationId(Long conversationId) {
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId);
    }
}