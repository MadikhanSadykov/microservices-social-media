package com.madikhan.chatmicro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableJpaRepositories("com.madikhan.chatmicro.repository")
@EnableEurekaClient
public class ChatMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatMicroserviceApplication.class, args);
    }

}
