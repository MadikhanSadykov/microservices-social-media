package com.madikhan.chatmicro.repository;

import com.madikhan.chatmicro.model.ChatMessage;
import com.madikhan.chatmicro.model.enums.MessageStatus;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository
        extends MongoRepository<ChatMessage, String> {

    Long countBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId);

}