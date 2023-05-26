package com.madikhan.imagemicro.service;

import com.madikhan.imagemicro.model.ProfileImage;
import com.madikhan.imagemicro.repository.ProfileImageRepository;
import com.madikhan.imagemicro.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileImageServiceImpl {

    private final ProfileImageRepository profileImageRepository;

    public byte[] uploadProfileImage(MultipartFile file, String uuid) throws IOException {

        return profileImageRepository.save(ProfileImage.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .uuid(uuid)
                .imageData(ImageUtils.compressImage(file.getBytes())).build())
                .getImageData();
    }

    @Transactional
    public byte[] getProfileImageByUuid(String uuid) {
        Optional<ProfileImage> profileImage = profileImageRepository.findProfileImageByUuid(uuid);

        if (profileImage.isEmpty()) {
            throw new EntityNotFoundException("Image with uuid " + uuid + " not found");
        }

        return ImageUtils.decompressImage(profileImage.get().getImageData());
    }

    public void deleteProfileImageByUuid(String uuid) {
        profileImageRepository.deleteProfileImageByUuid(uuid);
    }

}
