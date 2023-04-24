package com.madikhan.imagemicro.service;

import com.madikhan.imagemicro.model.ProfileImage;
import com.madikhan.imagemicro.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public void addImage(String uuid, String name, MultipartFile file) throws IOException {
        ProfileImage profileImage = new ProfileImage();
        profileImage.setUuid(uuid);
        profileImage.setName(name);
        profileImage.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
    }

    public ProfileImage getImage(String id) {
        return imageRepository.findById(id).get();
    }

    public List<ProfileImage> getAllByUuid(String uuid) {
        return imageRepository.findProfileImagesByUuid(uuid);
    }

    public void removeById(String id) {
        imageRepository.deleteById(id);
    }

}
