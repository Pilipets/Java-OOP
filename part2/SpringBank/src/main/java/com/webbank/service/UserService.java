package com.webbank.service;

import com.webbank.dao.UserDao;
import com.webbank.dao.UserProfileDao;
import com.webbank.model.User;
import com.webbank.model.UserProfile;
import com.webbank.model.UserProfileType;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.jose.jwk.JWK;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	@Autowired
	private UserProfileDao userProfileDao;


	public void saveUser(User user) {
		userDao.save(user);
	}

	public boolean isUserNameUnique(String username) {
		User user = userDao.findByUsername(username);
		return (user == null);
	}

	public User getContextUser() {
		Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		KeycloakPrincipal principal = null;
		if (object instanceof KeycloakPrincipal) {
			principal = ((KeycloakPrincipal)object);
		}
		AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();
		String userName = accessToken.getPreferredUsername();
		if(userDao.findByUsername(userName) == null) {
			User user = new User();
			user.setName(accessToken.getName());
			user.setUsername(accessToken.getPreferredUsername());
			user.setEnabled(true);

			AccessToken.Access realmAccess = accessToken.getRealmAccess();
			Set<String> roles = realmAccess.getRoles();
			Set<UserProfile> userRoles = new HashSet<>();
			if(roles.contains("USER")) {
				userRoles.add(userProfileDao.findByType("USER"));
			}
			user.setUserProfiles(userRoles);
			userDao.save(user);
		}
		return userDao.findByUsername(accessToken.getPreferredUsername());
	}

	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}
}
