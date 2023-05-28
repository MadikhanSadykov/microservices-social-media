package com.madikhan.imagemicro.controller;

import com.madikhan.imagemicro.service.ProfileImageServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Api("Image microservice endpoint")
public class ProfileImageController {

    private final ProfileImageServiceImpl profileImageService;

    @PostMapping(value = "/{uuid}/add/avatar", produces = {"image/jpg", "image/jpeg", "image/png"})
    @ApiOperation("Add avatar by uuid")
    public ResponseEntity<?> addAvatar(@PathVariable(name = "uuid") String uuid,
                                      @RequestParam(name = "image") MultipartFile image) {
        try {
            byte[] imageData = profileImageService.uploadProfileImage(image, uuid);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(imageData);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/avatar/{uuid}", produces = {"image/jpg", "image/jpeg", "image/png"})
    @ApiOperation("Get Byte Array image by uuid")
    public ResponseEntity<?> getAvatarByUuid(@PathVariable(name = "uuid") String uuid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(profileImageService.getProfileImageByUuid(uuid));
    }

    @PostMapping("/remove/avatar/{uuid}")
    @ApiOperation("Delete avatar by uuid")
    public ResponseEntity<?> removeAvatarByUuid(@PathVariable(name = "uuid") String uuid) {
        profileImageService.deleteProfileImageByUuid(uuid);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully removed");
    }

}
