package com.madikhan.profilemicro.controller;

import com.madikhan.profilemicro.dto.ProfileDTO;
import com.madikhan.profilemicro.dto.ProfileRecommendationDTO;
import com.madikhan.profilemicro.mapper.ProfileMapper;
import com.madikhan.profilemicro.model.request.ProfileUpdateRequest;
import com.madikhan.profilemicro.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@Api("Profile microservice endpoint")
public class ProfileController {

    private final Environment environment;
    private final ProfileMapper profileMapper;
    private final ProfileService profileService;

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Partial updating profile (username, firstName, lastName, bio, location, gender)")
    public ProfileDTO updateProfile(@Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        return profileService.update(profileUpdateRequest);
    }

    @GetMapping("/id/{id}")
    @ApiOperation("Get profile by id")
    public ResponseEntity<ProfileDTO> getById(@PathVariable Long id) {
        return new ResponseEntity<>(profileMapper
                .profileToDTO(
                  profileService.listById(id)
                ), HttpStatus.OK);
    }

    @GetMapping()
    @ApiOperation("Get all profiles")
    public List<ProfileDTO> getAll() {
        return profileMapper
                .profilesToDTOList(
                        profileService.listAll()
                );
    }

    @GetMapping("/recommend/{username}")
    @ApiOperation("Get recommendation for profiles based on interests, " +
            "it returns the sorted Map with number of matching interests")
    public ResponseEntity<List<ProfileRecommendationDTO>> getRecommendationsByUsername(
            @PathVariable(name = "username") String username) {
        return new ResponseEntity<>(
                profileService.getProfilesRecommendationListBySameInterests(username),
                HttpStatus.OK
        );
    }

    @GetMapping("/uuid/{uuid}")
    @ApiOperation("Get profile by uuid")
    public ResponseEntity<ProfileDTO> getByUUid(@PathVariable(name = "uuid") String uuid) {
        return new ResponseEntity<>(profileMapper
                .profileToDTO(
                        profileService.listByUuid(uuid)
                ), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @ApiOperation("Get profile by username")
    public ResponseEntity<ProfileDTO> getByUsername(@PathVariable(name = "username") String username) {
        return new ResponseEntity<>(profileMapper
                .profileToDTO(
                        profileService.listByUsername(username)
                ), HttpStatus.OK);
    }

//    @PostMapping("/send/{senderUuid}/to/{targetUuid}")
//    @ApiOperation("Send request to friend")
//    public ResponseEntity<?> sendRequestToFriend(
//            @PathVariable(name = "senderUuid") String senderUuid,
//            @PathVariable(name = "targetUuid") String targetUuid) {
//
//    }

}
