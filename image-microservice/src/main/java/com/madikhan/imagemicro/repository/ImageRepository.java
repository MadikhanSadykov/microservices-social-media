package com.madikhan.imagemicro.repository;

import com.madikhan.imagemicro.model.ProfileImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImageRepository extends MongoRepository<ProfileImage, String> {

    ProfileImage findProfileImageByUuid(String uuid);

    List<ProfileImage> findProfileImagesByUuid(String uuid);

}
