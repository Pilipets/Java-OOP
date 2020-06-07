package com.webbank.service;

import com.webbank.dao.UserDao;
import com.webbank.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
	@Autowired
	private UserDao dao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		dao.save(user);
	}

	public boolean isUserNameUnique(String username) {
		User user = dao.findByUsername(username);
		return (user == null);
	}

	public User findByUsername(String username) {
		return dao.findByUsername(username);
	}
}
