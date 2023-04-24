package com.madikhan.profilemicro.service.impl;

import com.madikhan.profilemicro.dto.ProfileDTO;
import com.madikhan.profilemicro.mapper.ProfileMapper;
import com.madikhan.profilemicro.model.entity.Interest;
import com.madikhan.profilemicro.model.entity.Profile;
import com.madikhan.profilemicro.model.request.ProfileUpdateRequest;
import com.madikhan.profilemicro.model.request.RegisterRequest;
import com.madikhan.profilemicro.repository.ProfileRepository;
import com.madikhan.profilemicro.service.ProfileService;
import com.madikhan.profilemicro.utils.DescendingIntegerComparator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProfileServiceImpl implements UserDetailsService, ProfileService {

    private final ProfileMapper profileMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final ProfileRepository profileRepository;

    public Map<Integer, ProfileDTO> getProfilesSortedBySameInterests(String username) {
        Map<Integer, ProfileDTO> profileMatchingSortedMap = new TreeMap<>(new DescendingIntegerComparator());

        List<Profile> profiles = profileRepository.findAll();
        Profile currentProfile = profileRepository.findProfileByUsername(username);

        Iterator<Profile> profileIterator = profiles.iterator();
        while (profileIterator.hasNext()) {
            ProfileDTO profileDTO = profileMapper.profileToDTO(profileIterator.next());

            List<Interest> currentProfileInterest = new ArrayList<>(currentProfile.getInterests());
            currentProfileInterest.retainAll(profileDTO.getInterests());

            if (currentProfileInterest.size() > 0) {
                profileMatchingSortedMap.put(currentProfileInterest.size(), profileDTO);
            }
        }
        return profileMatchingSortedMap;
    }

    public ProfileDTO register(RegisterRequest registerRequest) {

        Profile profile = profileMapper.registerRequestToProfile(registerRequest);
        profile.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        profile.setUuid(UUID.randomUUID().toString());
        profileRepository.save(profile);

        return profileMapper.profileToDTO(profile);
    }

    public ProfileDTO update(ProfileUpdateRequest profileUpdateRequest) {
        String uuid = profileUpdateRequest.getUuid();
        Profile profile = profileRepository.findProfileByUuid(uuid);

        if (ObjectUtils.isEmpty(profile)) {
            throw new UsernameNotFoundException("Profile not found");
        }

        profile.setFirstName(profileUpdateRequest.getFirstName());
        profile.setLastName(profileUpdateRequest.getLastName());
        profile.setBio(profileUpdateRequest.getBio());
        profile.setUsername(profileUpdateRequest.getUsername());
        profile.setLocation(profileUpdateRequest.getLocation());
        profile.setGender(profileUpdateRequest.getGender());
        profileRepository.save(profile);

        return profileMapper.profileToDTO(profile);
    }

    public Profile listById(Long id) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);

        if (optionalProfile.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found");
        }

        return profileRepository.findById(id).orElseGet(Profile::new);
    }

    public Profile listByUuid(String uuid) {
        Profile optionalProfile = profileRepository.findProfileByUuid(uuid);

        if (ObjectUtils.isEmpty(optionalProfile)) {
            throw new UsernameNotFoundException("Profile not found");
        }

        return optionalProfile;
    }

    public Profile listByUsername(String username) {
        Profile optionalProfile = profileRepository.findProfileByUsername(username);

        if (ObjectUtils.isEmpty(optionalProfile)) {
            throw new UsernameNotFoundException("Profile not found");
        }

        return optionalProfile;
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
