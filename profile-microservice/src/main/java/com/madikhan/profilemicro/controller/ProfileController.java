package com.madikhan.profilemicro.controller;

import com.madikhan.profilemicro.dto.ProfileDTO;
import com.madikhan.profilemicro.dto.ProfileRecommendationDTO;
import com.madikhan.profilemicro.mapper.ProfileMapper;
import com.madikhan.profilemicro.model.entity.Profile;
import com.madikhan.profilemicro.model.request.ProfileUpdateRequest;
import com.madikhan.profilemicro.service.ProfileService;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@Api("Profile microservice endpoint")
public class ProfileController {

    private final ProfileMapper profileMapper;
    private final ProfileService profileService;

    @PostMapping("/update")
    @ApiOperation("Partial updating Post profile (username, firstName, lastName, bio, location, gender)")
    public ProfileDTO updateProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest) {
        return profileService.update(profileUpdateRequest);
    }

    @PatchMapping()
    @ApiOperation("Partial updating Patch profile (username, firstName, lastName, bio, location, gender)")
    public ProfileDTO updatePatchProfile(@RequestBody ProfileUpdateRequest profileUpdateRequest) {
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
    @Timed(value = "getByUsername.time", description = "Time taken to return Profile by username")
    public ResponseEntity<ProfileDTO> getByUsername(@PathVariable(name = "username") String username) {
        return new ResponseEntity<>(profileMapper
                .profileToDTO(
                        profileService.listByUsername(username)
                ), HttpStatus.OK);
    }

    @PostMapping("/send/{senderUuid}/to/{targetUuid}")
    @ApiOperation("Send request to friend")
    public ResponseEntity<?> sendRequestToFriend(
            @PathVariable(name = "senderUuid") String senderUuid,
            @PathVariable(name = "targetUuid") String targetUuid) throws Exception {
        Profile profile = profileService.sendRequestToFriend(senderUuid, targetUuid);
        return new ResponseEntity<>(profileMapper.profileToDTO(profile), HttpStatus.OK);
    }

    @PostMapping("/accept/{senderUuid}/to/{accepterUuid}")
    @ApiOperation("Accept request to friend")
    public ResponseEntity<?> acceptRequestToFriend(
            @PathVariable(name = "senderUuid") String senderUuid,
            @PathVariable(name = "accepterUuid") String accepterUuid) throws Exception {
        Profile profile = profileService.acceptRequestToFriend(senderUuid, accepterUuid);
        return new ResponseEntity<>(profileMapper.profileToDTO(profile), HttpStatus.OK);
    }

    @PostMapping("/remove/request/{senderUuid}/to/{targetUuid}")
    @ApiOperation("Remove request to friend")
    public ResponseEntity<?> removeRequestToFriend(
            @PathVariable(name = "senderUuid") String senderUuid,
            @PathVariable(name = "targetUuid") String targetUuid) {
        Profile profile = profileService.removeRequestToFriend(senderUuid, targetUuid);
        return new ResponseEntity<>(profileMapper.profileToDTO(profile), HttpStatus.OK);
    }

    @PostMapping("/remove/friend/{firstUuid}/to/{secondUuid}")
    @ApiOperation("Delete friend")
    public ResponseEntity<?> removeFriend(
            @PathVariable(name = "firstUuid") String firstUuid,
            @PathVariable(name = "secondUuid") String secondUuid) {
        Profile profile = profileService.removeFriend(firstUuid, secondUuid);
        return new ResponseEntity<>(profileMapper.profileToDTO(profile), HttpStatus.OK);
    }

    @PostMapping("/reject/request/{senderUuid}/to/{myUuid}")
    @ApiOperation("Reject Request to friend")
    public ResponseEntity<?> rejectRequestToFriend(
            @PathVariable(name = "senderUuid") String senderUuid,
            @PathVariable(name = "myUuid") String myUuid) {
        Profile profile = profileService.removeFriend(senderUuid, myUuid);
        return new ResponseEntity<>(profileMapper.profileToDTO(profile), HttpStatus.OK);
    }

    @GetMapping("/check/friend/{firstUuid}/{secondUuid}")
    @ApiOperation("Check is profile in friends")
    public ResponseEntity<?> isProfileInFriends(
            @PathVariable(name = "firstUuid") String firstUuid,
            @PathVariable(name = "secondUuid") String secondUuid) {
        Boolean isFriends = profileService.isProfileInFriends(firstUuid, secondUuid);
        return new ResponseEntity<>(isFriends, HttpStatus.OK);
    }

    @GetMapping("/check/request/{senderUuid}/{targetUuid}")
    @ApiOperation("Check is profile sent request to friend")
    public ResponseEntity<?> isProfileSentRequestToFriend(
            @PathVariable(name = "senderUuid") String senderUuid,
            @PathVariable(name = "targetUuid") String targetUuid) {
        Boolean isFriends = profileService.isRequestToFriendSent(senderUuid, targetUuid);
        return new ResponseEntity<>(isFriends, HttpStatus.OK);
    }

    @GetMapping("/requests/to/{uuid}")
    @ApiOperation("Get List of profiles who sent request to me")
    public ResponseEntity<?>  getProfilesSentRequestToMe(@PathVariable(name = "uuid") String uuid) {
        Profile profile = profileService.listByUuid(uuid);
        return new ResponseEntity<>(profile.getRequestToMe(), HttpStatus.OK);
    }

    @GetMapping("/requests/from/{uuid}")
    @ApiOperation("Get List of profiles whom I sent request to friend")
    public ResponseEntity<?>  getProfilesSentRequestFromMe(@PathVariable(name = "uuid") String uuid) {
        Profile profile = profileService.listByUuid(uuid);
        return new ResponseEntity<>(profile.getRequestFromMe(), HttpStatus.OK);
    }

    @GetMapping("/friends/{uuid}")
    @ApiOperation("Get list of my friends")
    public ResponseEntity<?> getListOfMyFriends(@PathVariable(name = "uuid") String uuid) {
        Profile profile = profileService.listByUuid(uuid);
        return new ResponseEntity<>(profile.getFriends(), HttpStatus.OK);
    }
}
