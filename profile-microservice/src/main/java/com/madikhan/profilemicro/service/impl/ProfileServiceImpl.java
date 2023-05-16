package com.madikhan.profilemicro.service.impl;

import com.madikhan.profilemicro.dto.ProfileDTO;
import com.madikhan.profilemicro.dto.ProfileRecommendationDTO;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
    private final ProfileRepository profileRepository;

    @Override
    public List<ProfileRecommendationDTO> getProfilesRecommendationListBySameInterests(String username) {
        List<ProfileRecommendationDTO> profileRecommendationDTOList = new ArrayList<>();

        List<Profile> profiles = profileRepository.findAll();
        Optional<Profile> currentProfileOptional = profileRepository.findProfileByUsername(username);

        if (currentProfileOptional.isEmpty()) {
            return profileRecommendationDTOList;
        }

        Iterator<Profile> profileIterator = profiles.iterator();
        while (profileIterator.hasNext()) {
            ProfileRecommendationDTO profileRecommendationDTO = profileMapper
                    .profileToProfileRecommendationDTO(profileIterator.next());

            List<Interest> currentProfileInterest = new ArrayList<>(currentProfileOptional.get().getInterests());
            currentProfileInterest.retainAll(profileRecommendationDTO.getInterests());

            if (!currentProfileInterest.isEmpty()) {
                profileRecommendationDTO.getSameInterests().addAll(currentProfileInterest);
                profileRecommendationDTO.setNumberOfSameInterests(currentProfileInterest.size());
                profileRecommendationDTOList.add(profileRecommendationDTO);
            }
        }

        profileRecommendationDTOList
                .sort((leftValue, rightValue) -> rightValue.getNumberOfSameInterests()
                        .compareTo(leftValue.getNumberOfSameInterests()));

        return profileRecommendationDTOList;
    }

    @Override
    public ProfileDTO register(RegisterRequest registerRequest) {

        Profile profile = profileMapper.registerRequestToProfile(registerRequest);
        profile.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        profile.setUuid(UUID.randomUUID().toString());
        profileRepository.save(profile);

        return profileMapper.profileToDTO(profile);
    }

    @Override
    public ProfileDTO update(ProfileUpdateRequest profileUpdateRequest) {
        String uuid = profileUpdateRequest.getUuid();
        Optional<Profile> profileOptional = profileRepository.findProfileByUuid(uuid);

        if (profileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found");
        }

        Profile profile = profileOptional.get();
        profile.setFirstName(profileUpdateRequest.getFirstName());
        profile.setLastName(profileUpdateRequest.getLastName());
        profile.setBio(profileUpdateRequest.getBio());
        profile.setUsername(profileUpdateRequest.getUsername());
        profile.setLocation(profileUpdateRequest.getLocation());
        profile.setGender(profileUpdateRequest.getGender());
        profileRepository.save(profile);

        return profileMapper.profileToDTO(profile);
    }

    @Override
    public Profile listById(Long id) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);

        if (optionalProfile.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found");
        }

        return optionalProfile.get();
    }

    @Override
    public Profile listByUuid(String uuid) {
        Optional<Profile> optionalProfile = profileRepository.findProfileByUuid(uuid);

        if (optionalProfile.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found");
        }

        return optionalProfile.get();
    }

    @Override
    public Profile listByUsername(String username) {
        Optional<Profile> optionalProfile = profileRepository.findProfileByUsername(username);

        if (optionalProfile.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found");
        }

        return optionalProfile.get();
    }

    @Override
    public List<Profile> listAll() {
        return profileRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Profile> profileOptional = profileRepository.findProfileByEmail(username);
        if (profileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found with username: " + username);
        }
        return new User(
                profileOptional.get().getEmail(),
                profileOptional.get().getPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>());
    }

    @Override
    public Profile getByEmail(String email) {
        Optional<Profile> profileOptional = profileRepository.findProfileByEmail(email);
        if (profileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found with email: " + email);
        }
        return profileOptional.get();
    }

    @Override
    public Profile removeAllInterestsByUsername(String username) {
        Optional<Profile> profileOptional = profileRepository.findProfileByUsername(username);
        if (profileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found with username: " + username);
        }
        Profile profile = profileOptional.get();
        profile.getInterests().clear();
        return profileRepository.save(profile);
    }

    @Override
    public Profile sendRequestToFriend(String senderUuid, String targetUuid) {
        Optional<Profile> senderProfileOptional = profileRepository.findProfileByUuid(senderUuid);
        Optional<Profile> targetProfileOptional = profileRepository.findProfileByUuid(targetUuid);

        if (senderProfileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Profile sender not found with uuid: " + senderUuid);
        }

        if (targetProfileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Profile target not found with uuid: " + targetUuid);
        }

        Profile senderProfile = senderProfileOptional.get();
        Profile targetProfile = targetProfileOptional.get();

        senderProfile.getRequestFromMe().add(targetProfile);
        return profileRepository.save(senderProfile);
    }

    @Override
    public Profile removeRequestToFriend(String senderUuid, String targetUuid) {
        Optional<Profile> senderProfileOptional = profileRepository.findProfileByUuid(senderUuid);
        Optional<Profile> targetProfileOptional = profileRepository.findProfileByUuid(targetUuid);

        if (senderProfileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Profile sender not found with uuid: " + senderUuid);
        }

        if (targetProfileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Profile target not found with uuid: " + targetUuid);
        }

        Profile senderProfile = senderProfileOptional.get();
        Profile targetProfile = targetProfileOptional.get();

        senderProfile.getRequestFromMe().remove(targetProfile);
        return profileRepository.save(senderProfile);
    }

}
