package com.madikhan.imagemicro.model;

import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "profile_image")
@Data
public class ProfileImage {

    @Id
    private String id;

    private String name;

    private Binary image;

    private String uuid;

}
