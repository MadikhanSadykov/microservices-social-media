//package com.madikhan.imagemicro.repository;
//
//import org.assertj.core.api.AssertionInfo;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class ProfileImageRepositoryTest {
//
//    ProfileImageRepository profileImageRepository;
//
//    @Test
//    void testExistsProfileImageByUuidAndIsAvatarIsTrue() {
//        Boolean isExists = Optional.ofNullable(
//                profileImageRepository
//                        .existsProfileImageByUuidAndIsAvatarIsTrue("f0d535f7-3f0a-4fb7-bdef-50fe936dfc25")).get();
//
//        Assertions.assertThat(isExists).isTrue();
//    }
//
//}