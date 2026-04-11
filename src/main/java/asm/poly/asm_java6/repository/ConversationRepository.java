package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.enity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserId(Long userId);

    List<Conversation> findByStaffId(Long staffId);
}