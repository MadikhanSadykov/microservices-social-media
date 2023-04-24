package com.madikhan.profilemicro.controller;

import com.madikhan.profilemicro.model.entity.Interest;
import com.madikhan.profilemicro.model.entity.Profile;
import com.madikhan.profilemicro.repository.InterestRepository;
import com.madikhan.profilemicro.repository.ProfileRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interests")
@RequiredArgsConstructor
@Api("Interest controller")
public class InterestController {

    private final InterestRepository interestRepository;
    private final ProfileRepository profileRepository;

    @PostMapping("/save")
    @ApiOperation("Create new interest")
    public ResponseEntity<Interest> save(@RequestBody Interest interest) {
        return new ResponseEntity<>(interestRepository.save(interest), HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation("Get all interests")
    public ResponseEntity<List<Interest>> getAll() {
        return new ResponseEntity<>(interestRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @ApiOperation("Get profile's interests")
    public ResponseEntity<List<Interest>> getByUsername(@PathVariable(name = "username") String username) {
        return new ResponseEntity<>(interestRepository.findInterestsByProfilesUsername(username),HttpStatus.OK);
    }

    @PostMapping("/{username}/add")
    @ApiOperation("Add one interest to profile")
    public ResponseEntity<Profile> addInterestToProfile(@PathVariable("username") String username,
                                                        @RequestBody Interest interest) {
        Profile profile = profileRepository.findProfileByUsername(username);
        profile.getInterests().add(interest);
        return new ResponseEntity<>(profileRepository.save(profile), HttpStatus.OK);
    }

    @PostMapping("/{username}/add/list")
    @ApiOperation("Add list of interests to profile")
    public ResponseEntity<Profile> addInterestListToProfile(@PathVariable("username") String username,
                                                            @RequestBody List<Interest> interests) {
        Profile profile = profileRepository.findProfileByUsername(username);
        profile.getInterests().addAll(interests);
        return new ResponseEntity<>(profileRepository.save(profile), HttpStatus.OK);
    }

    @PostMapping("/{username}/remove")
    @ApiOperation("Remove one interest from profile")
    public ResponseEntity<Profile> removeInterestFromProfile(@PathVariable("username") String username,
                                                             @RequestBody Interest interest) {
        Profile profile = profileRepository.findProfileByUsername(username);
        profile.getInterests().remove(interest);
        return new ResponseEntity<>(profileRepository.save(profile), HttpStatus.OK);
    }

    @PostMapping("/{username}/remove/list")
    @ApiOperation("Remove list of interests from profile")
    public ResponseEntity<Profile> removeInterestListToProfile(@PathVariable("username") String username,
                                                               @RequestBody List<Interest> interests) {
        Profile profile = profileRepository.findProfileByUsername(username);
        profile.getInterests().removeAll(interests);
        return new ResponseEntity<>(profileRepository.save(profile), HttpStatus.OK);
    }

}
