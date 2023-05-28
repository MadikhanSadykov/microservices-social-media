package com.madikhan.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madikhan.notification.event.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private static final ObjectMapper mapper = new ObjectMapper();

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "notificationTopic")
    public void handleRequestToFriendNotification(Notification notification) throws Exception {
        // send some email or push notification

//        Notification notification = fromJson(notificationString, Notification.class);

        // /user/{uuid}/notifications
        messagingTemplate.convertAndSendToUser(
                notification.getTargetUuid(),
                "/notifications",
                notification
        );

        log.info("Received Notification - from {} to {}",
                notification.getSenderUuid(),
                notification.getTargetUuid());
    }

//    private <T> T fromJson(String json, Class<T> clazz) throws Exception {
//        try {
//            return mapper.readValue(json, clazz);
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }

}
