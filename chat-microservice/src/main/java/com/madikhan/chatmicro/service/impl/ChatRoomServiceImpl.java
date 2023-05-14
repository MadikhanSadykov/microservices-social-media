package com.madikhan.chatmicro.service.impl;

import com.madikhan.chatmicro.model.ChatRoom;
import com.madikhan.chatmicro.repository.ChatRoomRepository;
import com.madikhan.chatmicro.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Optional<String> getChatId(String senderId, String recipientId, boolean createIfNotExist) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(!createIfNotExist) {
                        return  Optional.empty();
                    }
                    var chatId =
                            String.format("%s_%s", senderId, recipientId);

                    ChatRoom senderRecipient = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .build();

                    ChatRoom recipientSender = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }
}
