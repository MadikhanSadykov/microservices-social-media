package com.madikhan.imagemicro.repository;

import com.madikhan.imagemicro.model.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Optional<ProfileImage> findProfileImageByUuid(String uuid);

    void deleteProfileImageByUuid(String uuid);

}
