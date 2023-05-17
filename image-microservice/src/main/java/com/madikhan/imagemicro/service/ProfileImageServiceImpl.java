package com.madikhan.imagemicro.service;

import com.madikhan.imagemicro.model.ProfileImage;
import com.madikhan.imagemicro.repository.ProfileImageRepository;
import com.madikhan.imagemicro.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
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

    public ProfileImage downloadProfileAvatarByUuid(String uuid) {
        Optional<ProfileImage> avatarOptional = profileImageRepository.findProfileImageByUuidAndIsAvatarIsTrue(uuid);
        if (avatarOptional.isEmpty()) {
            return new ProfileImage();
        }
        ProfileImage avatar = avatarOptional.get();
        avatar.setImageData(
                ImageUtils.decompressImage(avatar.getImageData()));
        return avatar;
    }

//    public List<ProfileImage> downloadProfileImagesByUuid(String uuid) {
//        Optional<List<ProfileImage>> optionalImages = profileImageRepository.findProfileImagesByUuid(uuid);
//
//        if (optionalImages.isEmpty()) {
//            return optionalImages.get();
//        }
//
//        List<ProfileImage> images = optionalImages.get();
//        for (ProfileImage profileImage : images) {
//            images.(
//                    ImageUtils.decompressImage(profileImage.getImageData())
//            );
//        }
//
//        return images;
//    }

    public void removeById(Long id) {
        profileImageRepository.deleteById(id);
    }

    public void removeAvatarByUuid(String uuid) {
        profileImageRepository.deleteAvatarUuid(uuid);
    }

    public List<ProfileImage> listByUuid(String uuid) {
        return profileImageRepository.findAllByUuid(uuid).get();
    }

}
