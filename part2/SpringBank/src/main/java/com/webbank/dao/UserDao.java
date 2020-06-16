package com.webbank.dao;

import com.webbank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Integer> {
    @Query("SELECT u FROM User u where u.username = ?1")
    User findByUsername(String username);
}
