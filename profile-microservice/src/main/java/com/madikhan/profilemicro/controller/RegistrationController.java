package com.madikhan.profilemicro.controller;

import com.madikhan.profilemicro.dto.ProfileDTO;
import com.madikhan.profilemicro.mapper.ProfileMapper;
import com.madikhan.profilemicro.model.request.RegisterRequest;
import com.madikhan.profilemicro.model.response.SaveProfileResponse;
import com.madikhan.profilemicro.service.impl.ProfileServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/register")
@RequiredArgsConstructor
@Api("Registration")
public class RegistrationController {

    private final ProfileMapper profileMapper;
    private final ProfileServiceImpl profileService;

    @PostMapping()
    @ApiOperation("Register new profile")
    public ResponseEntity<SaveProfileResponse> registerProfile(@Valid @RequestBody RegisterRequest registerRequest) {
        ProfileDTO profileDTO = profileService.register(registerRequest);
        SaveProfileResponse saveProfileResponse = profileMapper.dtoToSaveResponse(profileDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(saveProfileResponse);
    }

}
