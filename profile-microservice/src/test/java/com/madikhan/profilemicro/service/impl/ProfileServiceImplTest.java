//package com.madikhan.profilemicro.service.impl;
//
//import com.madikhan.profilemicro.dto.ProfileRecommendationDTO;
//import com.madikhan.profilemicro.model.entity.Profile;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class ProfileServiceImplTest {
//
//    @Autowired
//    ProfileServiceImpl profileService;
//
//
//
//    @Test
//    void getProfilesRecommendationListBySameInterests() {
//
//        List<ProfileRecommendationDTO> profiles =
//                profileService.getProfilesRecommendationListBySameInterests("testUser1");
//
//        Integer age = profiles.get(0).getAge();
//        System.out.println(age);
//
//        Assertions.assertThat(age).isNotNull();
//
//    }
//}