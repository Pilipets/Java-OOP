package com.webbank.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Entity
@Table(name = "BANK_STATS")
public class BankStats implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;

    @OneToOne(fetch = FetchType.EAGER,targetEntity = Bank.class)
    @JoinColumn(name = "BANK_ID")
    private Bank bank;

    @Column(name = "TOTAL_REPLENISH")
    private BigInteger totalReplenish;

    @Column(name = "TOTAL_TRANSFER")
    private BigInteger totalTransfer;

    @Column(name = "TOTAL_PROFIT")
    private BigInteger totalProfit;

    public BankStats() {
    }

    public BigInteger getTotalReplenish() {
        return totalReplenish;
    }

    public void setTotalReplenish(BigInteger totalReplenish) {
        this.totalReplenish = totalReplenish;
    }

    public BigInteger getTotalTransfer() {
        return totalTransfer;
    }

    public void setTotalTransfer(BigInteger totalTransfer) {
        this.totalTransfer = totalTransfer;
    }

    public BigInteger getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigInteger totalProfit) {
        this.totalProfit = totalProfit;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
