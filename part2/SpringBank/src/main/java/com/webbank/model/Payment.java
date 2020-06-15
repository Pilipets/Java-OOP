package com.webbank.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PAYMENTS")
public class Payment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name = "MONEY")
    private int money;
    @Column(name = "CLIENTID")
    private int clientId;
    @Column(name = "CARDNUMBER")
    private int cardNumber;
    @Column(name = "INFO")
    private String info;
    @Temporal(TemporalType.DATE)
    private Date date;

    public void setId(int id){this.id = id;}
    public int getId(){return id;}
    public void setMoney(int money){this.money = money;}
    public int getMoney(){return money;}
    public void setClient(int clientId){this.clientId = clientId;}
    public int getClient(){return clientId;}
    public void setCard(int cardNumber){this.cardNumber = cardNumber;}
    public int getCard(){return cardNumber;}
    public void setInfo(String info){this.info = info;}
    public String getInfo(){return info;}
    public void setDate(Date date){ this.date = date;}
    public Date getDate(){ return date;}
}
