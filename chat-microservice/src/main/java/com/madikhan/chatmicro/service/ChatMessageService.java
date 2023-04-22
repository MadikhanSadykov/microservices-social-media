package com.madikhan.chatmicro.service;

import com.madikhan.chatmicro.model.ChatMessage;
import com.madikhan.chatmicro.model.enums.MessageStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatMessageService {

    ChatMessage findById(String id);

    List<ChatMessage> findChatMessages(String senderId, String recipientId);

    void updateStatuses(String senderId, String recipientId, MessageStatus status);

    ChatMessage save(ChatMessage chatMessage);

    Long countNewMessages(String senderId, String recipientId);

}
