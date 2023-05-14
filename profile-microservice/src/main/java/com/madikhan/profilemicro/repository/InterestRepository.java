package com.madikhan.profilemicro.repository;

import com.madikhan.profilemicro.model.entity.Interest;
import com.madikhan.profilemicro.model.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> findInterestsByProfilesUsername(String username);

    @Modifying
    @Query("delete from profile_interest pi where pi.")
    void deleteAllByProfilesUsername(String username);
}
