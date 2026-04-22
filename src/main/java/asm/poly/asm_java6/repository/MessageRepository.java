package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.enity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);

    // Lấy tin nhắn cuối cùng (gần nhất) theo thời gian gửi
    Message findTopByConversationIdOrderBySentAtDesc(Long conversationId);
}