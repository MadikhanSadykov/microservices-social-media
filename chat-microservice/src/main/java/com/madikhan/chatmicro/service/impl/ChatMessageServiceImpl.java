package com.madikhan.chatmicro.service.impl;

import com.madikhan.chatmicro.exception.ResourceNotFoundException;
import com.madikhan.chatmicro.model.ChatMessage;
import com.madikhan.chatmicro.model.enums.MessageStatus;
import com.madikhan.chatmicro.repository.ChatMessageRepository;
import com.madikhan.chatmicro.repository.ChatRoomRepository;
import com.madikhan.chatmicro.service.ChatMessageService;
import com.madikhan.chatmicro.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    @Override
    public Long countNewMessages(String senderId, String recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, MessageStatus.RECEIVED);
    }

    @Override
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatId(senderId, recipientId, false);

        var messages =
                chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());

        if(messages.size() > 0) {
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

        return messages;
    }

    @Override
    public ChatMessage findById(Long id) {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    @Override
    public void updateStatuses(String senderId, String recipientId, MessageStatus status) {
        repository.updateStatusesBySenderIdAndRecipientId(senderId, recipientId, status);
    }

}
