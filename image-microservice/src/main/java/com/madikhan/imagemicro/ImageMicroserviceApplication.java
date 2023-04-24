package com.madikhan.imagemicro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
public class ImageMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageMicroserviceApplication.class, args);
    }

}
