package com.webbank.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ACCOUNTS")
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name = "MONEYAMOUNT")
    private int moneyAmount;
    @Column(name = "ISBLOCKED")
    private boolean isBlocked;
    public void setId(int id){this.id = id;}
    public int getId(){return id;}
    public void setMoneyAmount(int moneyAmount){this.moneyAmount = moneyAmount;}
    public int getMoneyAmount(){return moneyAmount;}
    public void setIsBlocked(boolean isBlocked){this.isBlocked = isBlocked;}
    public boolean getIsBlocked(){return isBlocked;}
}
