package com.madikhan.imagemicro.repository;

import com.madikhan.imagemicro.model.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Optional<ProfileImage> findByName(String name);

    Optional<List<ProfileImage>> findProfileImagesByUuid(String uuid);

    Optional<ProfileImage> findProfileImageByUuidAndIsAvatarIsTrue(String uuid);

    void deleteById(Long id);

    @Modifying
    @Query("delete from ProfileImage p where p.uuid =:uuid and p.isAvatar=true")
    void deleteAvatarUuid(@Param("uuid") String uuid);

    Optional<List<ProfileImage>> findAllByUuid(String uuid);

}
