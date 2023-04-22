package com.madikhan.profilemicro.controller;

import com.madikhan.profilemicro.model.entity.Interest;
import com.madikhan.profilemicro.model.entity.Profile;
import com.madikhan.profilemicro.repository.InterestRepository;
import com.madikhan.profilemicro.repository.ProfileRepository;
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
public class InterestController {

    private final InterestRepository interestRepository;
    private final ProfileRepository profileRepository;

    @PostMapping("/save")
    public ResponseEntity<Interest> save(@RequestBody Interest interest) {
        return new ResponseEntity<>(interestRepository.save(interest), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Interest>> getAll() {
        return new ResponseEntity<>(interestRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Interest>> getByUsername(@PathVariable(name = "username") String username) {
        return new ResponseEntity<>(interestRepository.findInterestsByProfilesUsername(username),HttpStatus.OK);
    }

}
