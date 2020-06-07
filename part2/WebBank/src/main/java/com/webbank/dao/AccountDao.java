package com.webbank.dao;

import com.webbank.model.Account;
import com.webbank.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
@Transactional
public interface AccountDao extends CrudRepository<Account, Integer> {
    @Query("SELECT a FROM Account a WHERE a.id = ?1")
    Account getAccount(int accId);
}
