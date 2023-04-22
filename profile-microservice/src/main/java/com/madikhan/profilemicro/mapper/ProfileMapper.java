package com.madikhan.profilemicro.mapper;

import com.madikhan.profilemicro.dto.ProfileDTO;
import com.madikhan.profilemicro.model.entity.Profile;
import com.madikhan.profilemicro.model.request.RegisterRequest;
import com.madikhan.profilemicro.model.response.SaveProfileResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfileMapper {

    private final ModelMapper modelMapper;

    public ProfileDTO profileToDTO(Profile profile) {
        return modelMapper.map(profile, ProfileDTO.class);
    }

    public ProfileDTO registerRequestToDTO(RegisterRequest registerRequest) {
        return modelMapper.map(registerRequest, ProfileDTO.class);
    }

    public List<ProfileDTO> profilesToDTOList(List<Profile> profiles) {
        Type listType = new TypeToken<List<ProfileDTO>>(){}.getType();
        return modelMapper.map(profiles, listType);
    }

    public Profile dtoToProfile(ProfileDTO profileDTO) {
        return modelMapper.map(profileDTO, Profile.class);
    }

    public Profile registerRequestToProfile(RegisterRequest registerRequest) {
        return modelMapper.map(registerRequest, Profile.class);
    }

    public SaveProfileResponse dtoToSaveResponse(ProfileDTO profileDTO) {
        return modelMapper.map(profileDTO, SaveProfileResponse.class);
    }

    public List<Profile> dtoListToProfiles(List<ProfileDTO> profileDTOList) {
        Type listType = new TypeToken<List<Profile>>(){}.getType();
        return modelMapper.map(profileDTOList, listType);
    }

}
