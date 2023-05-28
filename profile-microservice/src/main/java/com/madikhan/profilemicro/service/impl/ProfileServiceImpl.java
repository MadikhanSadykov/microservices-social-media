package com.madikhan.profilemicro.service.impl;

import com.madikhan.profilemicro.dto.ProfileDTO;
import com.madikhan.profilemicro.dto.ProfileRecommendationDTO;
import com.madikhan.profilemicro.mapper.ProfileMapper;
import com.madikhan.profilemicro.model.entity.Interest;
import com.madikhan.profilemicro.model.entity.Profile;
import com.madikhan.profilemicro.model.event.Notification;
import com.madikhan.profilemicro.model.request.ProfileUpdateRequest;
import com.madikhan.profilemicro.model.request.RegisterRequest;
import com.madikhan.profilemicro.repository.ProfileRepository;
import com.madikhan.profilemicro.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProfileServiceImpl implements UserDetailsService, ProfileService {

    private final ProfileMapper profileMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final KafkaTemplate<String, Notification> kafkaTemplate;

    private List<Profile> getFirstAndSecondProfilesByUuids(String firstUuid, String secondUuid) 
            throws UsernameNotFoundException {
        Optional<Profile> firstProfileOptional = profileRepository.findProfileByUuid(firstUuid);
        Optional<Profile> secondProfileOptional = profileRepository.findProfileByUuid(secondUuid);

        if (firstProfileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Profile sender not found with uuid: " + firstUuid);
        }

        if (secondProfileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Profile target not found with uuid: " + secondUuid);
        }

        List<Profile> profiles = new ArrayList<>();
        profiles.add(0, firstProfileOptional.get());
        profiles.add(1, secondProfileOptional.get());

        return profiles;
    }
    
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
                if (!profileRecommendationDTO.getUuid().equals(currentProfileOptional.get().getUuid())) {
                    profileRecommendationDTOList.add(profileRecommendationDTO);
                }
            }
        }


        profileRecommendationDTOList.removeAll(currentProfileOptional.get().getFriends());


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
    public ProfileDTO update(ProfileUpdateRequest profileUpdateRequest) throws UsernameNotFoundException {
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
        profile.setAge(profileUpdateRequest.getAge());
        profileRepository.save(profile);

        return profileMapper.profileToDTO(profile);
    }

    @Override
    public Profile listById(Long id) throws UsernameNotFoundException {
        Optional<Profile> optionalProfile = profileRepository.findById(id);

        if (optionalProfile.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found");
        }

        return optionalProfile.get();
    }

    @Override
    public Profile listByUuid(String uuid) throws UsernameNotFoundException {
        Optional<Profile> optionalProfile = profileRepository.findProfileByUuid(uuid);

        if (optionalProfile.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found");
        }

        return optionalProfile.get();
    }

    @Override
    public Profile listByUsername(String username) throws UsernameNotFoundException {
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
    public Profile getByEmail(String email) throws UsernameNotFoundException {
        Optional<Profile> profileOptional = profileRepository.findProfileByEmail(email);
        if (profileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found with email: " + email);
        }
        return profileOptional.get();
    }

    @Override
    public Profile removeAllInterestsByUsername(String username) throws UsernameNotFoundException {
        Optional<Profile> profileOptional = profileRepository.findProfileByUsername(username);
        if (profileOptional.isEmpty()) {
            throw new UsernameNotFoundException("Profile not found with username: " + username);
        }
        Profile profile = profileOptional.get();
        profile.getInterests().clear();
        return profileRepository.save(profile);
    }

    @Override
    public Profile sendRequestToFriend(String senderUuid, String targetUuid) throws Exception {
        List<Profile> profiles = getFirstAndSecondProfilesByUuids(senderUuid, targetUuid);

        Profile senderProfile = profiles.get(0);
        Profile targetProfile = profiles.get(1);

        if (senderProfile.getRequestToMe().contains(targetProfile)) {
            senderProfile.getRequestToMe().remove(targetProfile);
            targetProfile.getRequestFromMe().remove(senderProfile);
            senderProfile.getFriends().add(targetProfile);
            targetProfile.getFriends().add(senderProfile);
            profileRepository.save(targetProfile);
        } else {
            senderProfile.getRequestFromMe().add(targetProfile);
        }

        Notification notification = Notification
                .builder()
                .id(senderUuid + "_" + targetUuid)
                .content("New Request To Friend from: " + senderProfile.getUsername())
                .senderUuid(senderUuid)
                .senderUsername(senderProfile.getUsername())
                .targetUuid(targetUuid)
                .targetUsername(targetProfile.getUsername())
                .build();

        // send notification to kafka server
        kafkaTemplate.send("notificationTopic", notification);

        return profileRepository.save(senderProfile);
    }

    @Override
    public Profile removeRequestToFriend(String senderUuid, String targetUuid) throws UsernameNotFoundException {
        List<Profile> profiles = getFirstAndSecondProfilesByUuids(senderUuid, targetUuid);

        Profile senderProfile = profiles.get(0);
        Profile targetProfile = profiles.get(1);

        senderProfile.getRequestFromMe().remove(targetProfile);
        return profileRepository.save(senderProfile);
    }

    @Override
    public Profile rejectRequestToMe(String senderUuid, String myUuid) throws UsernameNotFoundException {
        List<Profile> profiles = getFirstAndSecondProfilesByUuids(senderUuid, myUuid);

        Profile senderProfile = profiles.get(0);
        Profile myProfile = profiles.get(1);

        myProfile.getRequestToMe().remove(senderProfile);
        return profileRepository.save(myProfile);
    }

    @Override
    public Profile acceptRequestToFriend(String senderUuid, String targetUuid) throws Exception {
        List<Profile> profiles = getFirstAndSecondProfilesByUuids(senderUuid, targetUuid);

        Profile senderProfile = profiles.get(0);
        Profile targetProfile = profiles.get(1);

        senderProfile.getRequestFromMe().remove(targetProfile);
        senderProfile.getRequestToMe().remove(targetProfile);

        targetProfile.getRequestToMe().remove(senderProfile);
        targetProfile.getRequestFromMe().remove(senderProfile);

        senderProfile.getFriends().add(targetProfile);
        targetProfile.getFriends().add(senderProfile);

        profileRepository.save(senderProfile);

        Notification notification = Notification
                .builder()
                .id(senderUuid + "_" + targetUuid)
                .content("New Request To Friend from: " + senderProfile.getUsername())
                .senderUuid(senderUuid)
                .senderUsername(senderProfile.getUsername())
                .targetUuid(targetUuid)
                .targetUsername(targetProfile.getUsername())
                .build();

        // send notification to kafka server
        kafkaTemplate.send("notificationTopic", notification);

        return profileRepository.save(targetProfile);
    }

    @Override
    public Profile removeFriend(String senderUuid, String targetUuid) throws UsernameNotFoundException {
        List<Profile> profiles = getFirstAndSecondProfilesByUuids(senderUuid, targetUuid);

        Profile senderProfile = profiles.get(0);
        Profile targetProfile = profiles.get(1);

        senderProfile.getFriends().remove(targetProfile);
        targetProfile.getFriends().remove(senderProfile);

        profileRepository.save(targetProfile);
        return profileRepository.save(senderProfile);
    }

    @Override
    public Boolean isProfileInFriends(String senderUuid, String targetUuid) throws UsernameNotFoundException {
        List<Profile> profiles = getFirstAndSecondProfilesByUuids(senderUuid, targetUuid);

        Profile senderProfile = profiles.get(0);
        Profile targetProfile = profiles.get(1);

        if (senderProfile.getFriends().contains(targetProfile)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @Override
    public Boolean isRequestToFriendSent(String senderUuid, String targetUuid) throws UsernameNotFoundException {
        List<Profile> profiles = getFirstAndSecondProfilesByUuids(senderUuid, targetUuid);

        Profile senderProfile = profiles.get(0);
        Profile targetProfile = profiles.get(1);

        if (senderProfile.getRequestFromMe().contains(targetProfile)) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

}
