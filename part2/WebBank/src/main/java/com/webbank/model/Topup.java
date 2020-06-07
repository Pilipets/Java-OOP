package com.webbank.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TOPUP")
public class Topup {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name = "CARDNUMBER")
    private int cardNumber;
    @Column(name = "MONEY")
    private int money;
    @Temporal(TemporalType.DATE)
    private Date date;

    public void setId(int id){this.id = id;}
    public int getId(){return id;}
    public int getCardNumber(){return cardNumber;}
    public int getMoney(){return money;}
    public Date getDate(){ return date;}
    public void setCardNumber(int cardNumber){this.cardNumber = cardNumber;}
    public void setMoney(int money){ this.money = money;}
    public void setDate(Date date){ this.date = date;}
}
