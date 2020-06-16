package com.webbank.dao;

import com.webbank.model.Topup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface TopupDao extends CrudRepository<Topup, Integer> {
    @Query("select t from Topup t where t.cardNumber = ?1 and t.date between ?2 and ?3")
    List<Topup> getTopupByDate(int cardNumber, Date dateFrom, Date dateTo);

    @Query("select SUM(p.money) from Topup p where p.cardNumber IN ?1 and p.date between ?2 and ?3")
    Integer getSumByDate(List<Integer> cardNumber, Date dateFrom, Date dateTo);

    @Query("select t from Topup t where t.date > ?1 ORDER BY t.date ASC")
    Topup getFirstAfterDate(Date dateFrom);

    @Query("select t from Payment t where t.date < ?1 ORDER BY t.date DESC")
    Topup getLastBeforeDate(Date dateTo);
}
