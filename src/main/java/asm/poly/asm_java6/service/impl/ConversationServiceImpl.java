package asm.poly.asm_java6.service.impl;

import asm.poly.asm_java6.dto.ConversationInfoDTO;
import asm.poly.asm_java6.enity.Conversation;
import asm.poly.asm_java6.repository.ConversationRepository;
import asm.poly.asm_java6.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    @Override
    public List<ConversationInfoDTO> getAllConversationsWithInfo() {
        List<Object[]> rows = conversationRepository.getAllConversationsWithLastMessage();
        List<ConversationInfoDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            ConversationInfoDTO dto = new ConversationInfoDTO();
            dto.setId(row[0] != null ? ((Number) row[0]).longValue() : null);
            dto.setCustomerId(row[1] != null ? ((Number) row[1]).longValue() : null);
            dto.setCustomerName((String) row[2]);
            dto.setCustomerAvatar((String) row[3]);
            dto.setLastMessage((String) row[4]);
            // Nếu cột thời gian là java.sql.Timestamp
            if (row[5] != null) {
                dto.setLastMessageTime((java.time.LocalDateTime) row[5]);
            }
            result.add(dto);
        }
        return result;
    }

    @Override
    public ConversationInfoDTO getConversationInfoById(Long id) {
        // Có thể dùng lại query native hoặc lấy từ repository
        List<Object[]> rows = conversationRepository.getAllConversationsWithLastMessage();
        for (Object[] row : rows) {
            Long conversationId = row[0] != null ? ((Number) row[0]).longValue() : null;
            if (conversationId != null && conversationId.equals(id)) {
                ConversationInfoDTO dto = new ConversationInfoDTO();
                dto.setId(conversationId); // Đảm bảo trường là id
                dto.setCustomerId(row[1] != null ? ((Number) row[1]).longValue() : null);
                dto.setCustomerName((String) row[2]);
                dto.setCustomerAvatar((String) row[3]);
                dto.setLastMessage((String) row[4]);
                if (row[5] != null) {
                    dto.setLastMessageTime((java.time.LocalDateTime) row[5]);
                }
                return dto;
            }
        }
        return null; // hoặc throw exception nếu không tìm thấy
    }

    @Override
    public Optional<Conversation> findByUserIdAndStaffId(Long userId, long staffId) {
        return conversationRepository.findByUserIdAndStaffId(userId, staffId);
    }

    @Override
    public Optional<Conversation> findById(Long id) {
        return conversationRepository.findById(id);
    }


}