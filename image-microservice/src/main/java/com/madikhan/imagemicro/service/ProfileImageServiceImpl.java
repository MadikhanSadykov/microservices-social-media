package com.madikhan.imagemicro.service;

import com.madikhan.imagemicro.model.ProfileImage;
import com.madikhan.imagemicro.repository.ProfileImageRepository;
import com.madikhan.imagemicro.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileImageServiceImpl {

    private final ProfileImageRepository profileImageRepository;

    public String uploadProfileImage(MultipartFile file, String uuid, Boolean isAvatar) throws IOException {
        ProfileImage profileImage =  profileImageRepository.save(ProfileImage.builder()
                .name(file.getOriginalFilename())
                .uuid(uuid)
                .isAvatar(isAvatar)
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .build());

        return "File uploaded successfully : " + profileImage.getName();
    }

    public byte[] downloadProfileAvatarByUuid(String uuid) {
        Optional<ProfileImage> avatar = profileImageRepository.findProfileImageByUuidAndIsAvatarIsTrue(uuid);
        if (avatar.isEmpty()) {
            return new byte[0];
        }
        return ImageUtils.decompressImage(avatar.get().getImageData());
    }

    public List<byte[]> downloadProfileImagesByUuid(String uuid) {
        Optional<List<ProfileImage>> optionalImages = profileImageRepository.findProfileImagesByUuid(uuid);
        List<byte[]> images = new ArrayList<>();
        if (optionalImages.get().isEmpty()) {
            return images;
        }

        for (ProfileImage profileImage : optionalImages.get()) {
            images.add(
                    ImageUtils.decompressImage(profileImage.getImageData())
            );
        }

        return images;
    }

}
