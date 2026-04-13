package asm.poly.asm_java6.service;

import asm.poly.asm_java6.dto.ConversationInfoDTO;
import asm.poly.asm_java6.enity.Conversation;

import java.util.List;
import java.util.Optional;

public interface ConversationService {
    Conversation createConversation(Long userId, Long staffId);

    List<Conversation> getConversationsByUserId(Long userId);

    List<Conversation> getConversationsByStaffId(Long staffId);

    Conversation getById(Long id);

    List<Conversation> getAllConversations();

    List<ConversationInfoDTO> getAllConversationsWithInfo();

    ConversationInfoDTO getConversationInfoById(Long id);

    Optional<Conversation> findByUserIdAndStaffId(Long userId, long staffId);

    Optional<Conversation> findById(Long id);
}