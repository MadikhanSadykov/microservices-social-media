package com.madikhan.profilemicro.repository;

import com.madikhan.profilemicro.model.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Profile findProfileByUsername(String username);

    Profile findProfileByEmail(String email);

}
