package com.webbank.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "INTERVALS")
public class Intervals {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name = "MONEY")
    private String money;
    @Column(name = "MAXVAL")
    private int maxValue;
    @Temporal(TemporalType.DATE)
    private Date rightDate;
    @Temporal(TemporalType.DATE)
    private Date leftDate;
    @Temporal(TemporalType.DATE)
    private Date dateFrom;
    @Temporal(TemporalType.DATE)
    private Date dateTo;

    public void setId(int id){this.id = id;}
    public int getId(){return id;}
    public void setDateFrom(Date dateFrom){ this.dateFrom = dateFrom;}
    public Date getDateFrom(){ return dateFrom;}
    public void setDateTo(Date dateTo){ this.dateTo = dateTo;}
    public Date getDateTo(){ return dateTo;}
    public void setMoney(String money){ this.money = money;}
    public String getMoney(){ return money;}
    public void setLeftDate(Date leftDate){ this.leftDate = leftDate;}
    public Date getLeftDate(){ return leftDate;}
    public void setRightDate(Date rightDate){ this.rightDate = rightDate;}
    public Date getRightDate(){ return rightDate;}
    public void setMaxValue(int maxValue){ this.maxValue = maxValue;}
    public int getMaxValue(){ return maxValue;}
}
