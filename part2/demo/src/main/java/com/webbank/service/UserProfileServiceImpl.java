package com.webbank.service;

import com.webbank.dao.UserProfileDao;
import com.webbank.model.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;

@Service
public class UserProfileServiceImpl {
    @Resource
    private UserProfileDao dao;

    @Transactional
    public UserProfile findByType(String type) {
        return dao.findByType(type);
    }
}
