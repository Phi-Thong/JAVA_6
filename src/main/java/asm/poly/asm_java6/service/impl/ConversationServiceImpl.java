package asm.poly.asm_java6.service.impl;

import asm.poly.asm_java6.enity.Conversation;
import asm.poly.asm_java6.repository.ConversationRepository;
import asm.poly.asm_java6.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public Conversation createConversation(Long userId, Long staffId) {
        Conversation c = new Conversation();
        c.setUserId(userId);
        c.setStaffId(staffId);
        c.setCreatedAt(LocalDateTime.now());
        return conversationRepository.save(c);
    }

    @Override
    public List<Conversation> getConversationsByUserId(Long userId) {
        return conversationRepository.findByUserId(userId);
    }

    @Override
    public List<Conversation> getConversationsByStaffId(Long staffId) {
        return conversationRepository.findByStaffId(staffId);
    }

    @Override
    public Conversation getById(Long id) {
        return conversationRepository.findById(id).orElse(null);
    }
}