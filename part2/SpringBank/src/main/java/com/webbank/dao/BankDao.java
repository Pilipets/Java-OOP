package com.webbank.dao;

import com.webbank.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BankDao extends JpaRepository<Bank, Integer> {

    @Query("select bank from Bank bank join bank.admins admins where admins.id = ?1")
    Bank getBank(int id);

    @Query("select distinct bank from Bank bank join bank.accounts accounts where accounts.id = ?1")
    Bank getBankByAccount(int id);

    Bank findBankByName(String bankName);
}
