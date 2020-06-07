package com.webbank.dao;

import com.webbank.model.Bank;
import com.webbank.model.BankStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankStatsDao extends JpaRepository<BankStats, Integer> {
    BankStats getBankStatsByBank(Bank bank);
}