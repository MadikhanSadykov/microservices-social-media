package com.madikhan.notificaiton.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    // /push/messages
    @MessageMapping("/messages")
    @SendTo("/all/messages")
    public Message send(Message message) {
        return message;
    }

    // /push/private
    @MessageMapping("/private")
    public void sendToSpecificUser(@Payload Message message) {
        messagingTemplate.convertAndSendToUser("to", "/specific", message);
    }
}
