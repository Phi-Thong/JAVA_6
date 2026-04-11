package asm.poly.asm_java6.service;

import asm.poly.asm_java6.enity.Message;

import java.util.List;

public interface MessageService {
    Message sendMessage(Long conversationId, Long senderId, String content);

    List<Message> getMessagesByConversationId(Long conversationId);
}