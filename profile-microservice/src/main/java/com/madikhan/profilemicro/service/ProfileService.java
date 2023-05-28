package com.madikhan.profilemicro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madikhan.profilemicro.dto.ProfileDTO;
import com.madikhan.profilemicro.dto.ProfileRecommendationDTO;
import com.madikhan.profilemicro.model.entity.Profile;
import com.madikhan.profilemicro.model.request.ProfileUpdateRequest;
import com.madikhan.profilemicro.model.request.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProfileService {

    static final ObjectMapper mapper = new ObjectMapper();

    default <T> String toJson(T object) throws Exception {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    List<ProfileRecommendationDTO> getProfilesRecommendationListBySameInterests(String username)
            throws UsernameNotFoundException;

    ProfileDTO register(RegisterRequest registerRequest);

    ProfileDTO update(ProfileUpdateRequest profileUpdateRequest) throws UsernameNotFoundException;

    Profile listById(Long id) throws UsernameNotFoundException;

    Profile listByUuid(String uuid) throws UsernameNotFoundException;

    Profile listByUsername(String username) throws UsernameNotFoundException;

    List<Profile> listAll();

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Profile getByEmail(String email) throws UsernameNotFoundException;

    Profile removeAllInterestsByUsername(String username) throws UsernameNotFoundException;

    Profile sendRequestToFriend(String senderUuid, String targetUuid) throws Exception;

    Profile removeRequestToFriend(String senderUuid, String targetUuid) throws UsernameNotFoundException;

    Profile acceptRequestToFriend(String senderUuid, String targetUuid) throws Exception;

    Profile removeFriend(String senderUuid, String targetUuid) throws UsernameNotFoundException;

    Boolean isProfileInFriends(String senderUuid, String targetUuid) throws UsernameNotFoundException;

    Boolean isRequestToFriendSent(String senderUuid, String targetUuid) throws UsernameNotFoundException;

}
