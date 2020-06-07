package com.webbank.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CARDS")
public class Card {
    @Id
    @Column(name = "CARDNUMBER")
    private int cardNumber;
    @Column(name = "PIN")
    private int pin;
    @Column(name = "CLIENTID")
    private int clientId;
    @Column(name = "ACCID")
    private int accountId;
    public void setCardNumber(int cardNumber){this.cardNumber = cardNumber; }
    public int getCardNumber(){return cardNumber;}
    public void setPin(int pin){this.pin = pin;}
    public int getPin(){return pin;}
    public void setClientId(int clientId){this.clientId = clientId;}
    public int getClientId(){return clientId;}
    public void setAccountId(int accountId){this.accountId = accountId;}
    public int getAccountId(){return accountId;}
}
