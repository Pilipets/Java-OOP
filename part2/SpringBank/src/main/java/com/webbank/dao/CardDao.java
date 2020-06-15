package com.webbank.dao;

import com.webbank.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CardDao extends JpaRepository<Card, Integer> {
    @Query("SELECT c FROM  Card c where c.cardNumber=?1")
    Card getCard(int cardNum);
    @Query("SELECT c FROM Card c where c.clientId=?1")
    List<Card> getUsersCards(int clientId);
    @Query("select card from Card card join Account account on card.accountId = account.id" +
            " where account.isBlocked = true and card.accountId IN ?1")
    List<Card> getBlockedCards(List<Integer> accounts);

    @Query("select card from Card card join Account account on card.accountId = account.id" +
            " where card.accountId IN ?1")
    List<Card> getAllCards(List<Integer> accounts);

    // Card findByAccountId(Integer acc_id);
}
