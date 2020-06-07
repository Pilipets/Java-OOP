package com.webbank.service;

import com.webbank.dao.UserDao;
import com.webbank.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userDao.save(user);
	}

	public boolean isUserNameUnique(String username) {
		User user = userDao.findByUsername(username);
		return (user == null);
	}

	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}
}
