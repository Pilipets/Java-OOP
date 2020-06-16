package com.webbank.dao;


import com.webbank.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserProfileDao extends JpaRepository<UserProfile, Integer> {
    UserProfile findByType(String type);
}
