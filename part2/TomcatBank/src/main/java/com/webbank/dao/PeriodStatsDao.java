package com.webbank.dao;

import com.webbank.model.Payment;
import com.webbank.model.PeriodStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodStatsDao extends JpaRepository<PeriodStats, Integer> {
}
