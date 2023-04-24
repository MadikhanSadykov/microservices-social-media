package com.madikhan.chatmicro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("com.madikhan.chatmicro.repository")
public class ChatMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatMicroserviceApplication.class, args);
    }

}
