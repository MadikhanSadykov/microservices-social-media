package com.madikhan.profilemicro.service.impl;

import com.madikhan.profilemicro.dto.ProfileDTO;
import com.madikhan.profilemicro.mapper.ProfileMapper;
import com.madikhan.profilemicro.model.entity.Profile;
import com.madikhan.profilemicro.model.request.ProfileUpdateRequest;
import com.madikhan.profilemicro.model.request.RegisterRequest;
import com.madikhan.profilemicro.repository.ProfileRepository;
import com.madikhan.profilemicro.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProfileServiceImpl implements UserDetailsService, ProfileService {

    private final ProfileMapper profileMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final ProfileRepository profileRepository;

    private List<ProfileDTO> getBySameInterests(String uuid) {
        List<ProfileDTO> profilesWithSameInterests = new ArrayList<>();

        List<Profile> profiles = profileRepository.findAll();


        return profilesWithSameInterests;
    }

    public ProfileDTO register(RegisterRequest registerRequest) {

        Profile profile = profileMapper.registerRequestToProfile(registerRequest);
        profile.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        profile.setUuid(UUID.randomUUID().toString());
        profileRepository.save(profile);

        return profileMapper.profileToDTO(profile);
    }

    public ProfileDTO update(ProfileUpdateRequest profileUpdateRequest) {
        Long id = profileUpdateRequest.getId();
        Optional<Profile> optionalProfile = profileRepository.findById(id);

        if (optionalProfile.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found");
        }

        Profile profile = optionalProfile.get();
        profile.setFirstName(profileUpdateRequest.getFirstName());
        profile.setLastName(profileUpdateRequest.getLastName());
        profile.setBio(profileUpdateRequest.getBio());
        profile.setUsername(profileUpdateRequest.getUsername());
        profileRepository.save(profile);

        return profileMapper.profileToDTO(profile);
    }

    public Profile listById(Long id) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);

        if (optionalProfile.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found");
        }

        ProfileDTO profileDTO = profileMapper.profileToDTO(optionalProfile.get());
        String postServiceURL = "";


        return profileRepository.findById(id).orElseGet(Profile::new);
    }

    public List<Profile> listAll() {
        return profileRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Profile profile = profileRepository.findProfileByEmail(username);
        if (profile == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(
                profile.getEmail(),
                profile.getPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>());
    }

    public Profile getByEmail(String email) {
        return profileRepository.findProfileByEmail(email);
    }

}
