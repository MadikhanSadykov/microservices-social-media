package com.madikhan.profilemicro.service;

import com.madikhan.profilemicro.dto.ProfileDTO;
import com.madikhan.profilemicro.dto.ProfileRecommendationDTO;
import com.madikhan.profilemicro.model.entity.Profile;
import com.madikhan.profilemicro.model.request.ProfileUpdateRequest;
import com.madikhan.profilemicro.model.request.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ProfileService {

    List<ProfileRecommendationDTO> getProfilesRecommendationListBySameInterests(String username);

    ProfileDTO register(RegisterRequest registerRequest);

    ProfileDTO update(ProfileUpdateRequest profileUpdateRequest);

    Profile listById(Long id);

    Profile listByUuid(String uuid);

    Profile listByUsername(String username);

    List<Profile> listAll();

    UserDetails loadUserByUsername(String username);

    Profile getByEmail(String email);

    Profile removeAllInterestsByUsername(String username);

    Profile sendRequestToFriend(String senderUuid, String targetUuid);

    Profile removeRequestToFriend(String senderUuid, String targetUuid);

}
