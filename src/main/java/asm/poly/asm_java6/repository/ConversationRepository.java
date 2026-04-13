package asm.poly.asm_java6.repository;

import asm.poly.asm_java6.enity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUserId(Long userId);

    List<Conversation> findByStaffId(Long staffId);

    @Query(value = """
                SELECT
                    c.id AS conversationId,
                    u.id AS customerId,
                    u.ho_ten AS customerName,
                    u.avatar AS customerAvatar,
                    m.content AS lastMessage,
                    m.sent_at AS lastMessageTime
                FROM dbo.conversations c
                JOIN dbo.users u ON c.user_id = u.id
                OUTER APPLY (
                    SELECT TOP 1 m2.content, m2.sent_at
                    FROM dbo.messages m2
                    WHERE m2.conversation_id = c.id
                    ORDER BY m2.sent_at DESC
                ) m
            """, nativeQuery = true)
    List<Object[]> getAllConversationsWithLastMessage();

    Optional<Conversation> findByUserIdAndStaffId(Long userId, long staffId);

//    List<Object[]> findAllWithLastMessageNative();
}