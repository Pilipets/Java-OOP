package com.webbank.dao;

import com.webbank.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PaymentDao extends JpaRepository<Payment, Integer> {
    @Query("select p from Payment p where p.clientId = ?1")
    List<Payment> getPayments(int id);

    @Query("select p from Payment p where p.cardNumber = ?1 and p.date between ?2 and ?3")
    List<Payment> getPaymentsByDate(int cardNumber, Date dateFrom, Date dateTo);

    @Query("select SUM(p.money) from Payment p where p.cardNumber IN ?1 and p.date between ?2 and ?3")
    Integer getSumByDate(List<Integer> cardNumber, Date dateFrom, Date dateTo);

    @Query("select p from Payment p where p.date > ?1 ORDER BY p.date ASC")
    Payment getFirstAfterDate(Date dateFrom);

    @Query("select p from Payment p where p.date < ?1 ORDER BY p.date DESC")
    Payment getLastBeforeDate(Date dateTo);
}
