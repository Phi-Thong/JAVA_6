package asm.poly.asm_java6.service;

import asm.poly.asm_java6.enity.Conversation;

import java.util.List;

public interface ConversationService {
    Conversation createConversation(Long userId, Long staffId);

    List<Conversation> getConversationsByUserId(Long userId);

    List<Conversation> getConversationsByStaffId(Long staffId);

    Conversation getById(Long id);
}