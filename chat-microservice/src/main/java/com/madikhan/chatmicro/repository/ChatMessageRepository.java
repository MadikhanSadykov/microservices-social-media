package com.madikhan.chatmicro.repository;

import com.madikhan.chatmicro.model.ChatMessage;
import com.madikhan.chatmicro.model.enums.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Long countBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId);

    @Modifying
    @Query("UPDATE ChatMessage c SET c.status =:status WHERE c.senderId =:senderId AND c.recipientId =:recipientId")
    void updateStatusesBySenderIdAndRecipientId(@Param("senderId") String senderId,
                                                @Param("recipientId") String recipientId,
                                                @Param("status") MessageStatus status);

}
