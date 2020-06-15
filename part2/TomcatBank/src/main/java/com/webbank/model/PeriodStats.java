package com.webbank.model;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "PeriodStats")
public class PeriodStats {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date dateFrom;
    @Temporal(TemporalType.DATE)
    private Date dateTo;

    @Column(name = "TOTAL_REPLENISH")
    private BigInteger totalReplenish;

    @Column(name = "TOTAL_TRANSFER")
    private BigInteger totalTransfer;

    @Column(name = "InToOut")
    private Double inToOut;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
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

    public Double getInToOut() {
        return inToOut;
    }

    public void setInToOut(Double inToOut) {
        this.inToOut = inToOut;
    }
}
