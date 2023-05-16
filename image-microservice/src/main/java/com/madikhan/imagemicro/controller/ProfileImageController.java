package com.madikhan.imagemicro.controller;

import com.madikhan.imagemicro.model.ProfileImage;
import com.madikhan.imagemicro.service.ProfileImageServiceImpl;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Api("Image microservice endpoint")
public class ProfileImageController {

    private final ProfileImageServiceImpl profileImageService;

    @PostMapping("/{uuid}/add/avatar")
    public ResponseEntity<?> addAvatar(@PathVariable(name = "uuid") String uuid,
                                      @RequestParam(name = "image") MultipartFile image) {
        try {
            profileImageService.uploadProfileImage(image, uuid, Boolean.TRUE);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/{uuid}")
//    public ResponseEntity<List<byte[]>> getImagesBytesByUuid(@PathVariable(name = "uuid") String uuid) {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(profileImageService.downloadProfileImagesByUuid(uuid));
//    }

    @PostMapping("/remove/{id}")
    public ResponseEntity<?> removeImage(@PathVariable(name = "id") Long id) {
        profileImageService.removeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{uuid}/add")
    public ResponseEntity<?> uploadImage(@PathVariable(name = "uuid") String uuid,
                                         @RequestParam("image") MultipartFile image) {
        try {
            profileImageService.uploadProfileImage(image, uuid, Boolean.FALSE);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<List<ProfileImage>> getImagesByUuid(@PathVariable(name = "uuid") String uuid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(profileImageService.listByUuid(uuid));
    }

}
