package com.madikhan.profilemicro.repository;

import com.madikhan.profilemicro.model.entity.Interest;
import com.madikhan.profilemicro.model.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {

    List<Interest> findInterestsByProfilesUsername(String username);

}
