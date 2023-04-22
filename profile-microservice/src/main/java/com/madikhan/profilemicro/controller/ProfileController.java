package com.madikhan.profilemicro.controller;

import com.madikhan.profilemicro.dto.ProfileDTO;
import com.madikhan.profilemicro.mapper.ProfileMapper;
import com.madikhan.profilemicro.model.entity.Interest;
import com.madikhan.profilemicro.model.entity.Profile;
import com.madikhan.profilemicro.model.request.ProfileUpdateRequest;
import com.madikhan.profilemicro.repository.ProfileRepository;
import com.madikhan.profilemicro.service.impl.ProfileServiceImpl;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final Environment environment;
    private final ProfileMapper profileMapper;
    private final ProfileServiceImpl profileService;
    private final ProfileRepository profileRepository;

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProfileDTO updateProfile(@Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        return profileService.update(profileUpdateRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getById(@PathVariable Long id, HttpServletRequest request) {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");

        String subject = Jwts.parser()
                .setSigningKey(environment.getProperty("token.secret"))
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();

        System.out.println("=========================");
        System.out.println("=========================");
        System.out.println("uuid: " + subject);
        System.out.println("=========================");
        System.out.println("=========================");

        return new ResponseEntity<>(profileMapper
                .profileToDTO(
                  profileService.listById(id)
                ), HttpStatus.OK);
    }

    @GetMapping()
    public List<ProfileDTO> getAll() {
        return profileMapper
                .profilesToDTOList(
                        profileService.listAll()
                );
    }

    @PostMapping("/{username}/add/interest")
    public ResponseEntity<Profile> addInterestToProfile(@PathVariable("username") String username,
                                                        @RequestBody Interest interest) {
        Profile profile = profileRepository.findProfileByUsername(username);
        profile.getInterests().add(interest);
        return new ResponseEntity<>(profileRepository.save(profile), HttpStatus.OK);
    }

    @PostMapping("/{username}/add/interest/list")
    public ResponseEntity<Profile> addInterestListToProfile(@PathVariable("username") String username,
                                                        @RequestBody List<Interest> interests) {
        Profile profile = profileRepository.findProfileByUsername(username);
        profile.getInterests().addAll(interests);
        return new ResponseEntity<>(profileRepository.save(profile), HttpStatus.OK);
    }

    @PostMapping("/{username}/remove/interest")
    public ResponseEntity<Profile> removeInterestFromProfile(@PathVariable("username") String username,
                                                             @RequestBody Interest interest) {
        Profile profile = profileRepository.findProfileByUsername(username);
        profile.getInterests().remove(interest);
        return new ResponseEntity<>(profileRepository.save(profile), HttpStatus.OK);
    }

    @PostMapping("/{username}/add/interest/list")
    public ResponseEntity<Profile> removeInterestListToProfile(@PathVariable("username") String username,
                                                        @RequestBody List<Interest> interests) {
        Profile profile = profileRepository.findProfileByUsername(username);
        profile.getInterests().removeAll(interests);
        return new ResponseEntity<>(profileRepository.save(profile), HttpStatus.OK);
    }

    @GetMapping("/message")
    public ResponseEntity testMessage() {
        return new ResponseEntity("Test message", HttpStatus.OK);
    }

}
