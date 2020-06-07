package com.webbank.dao;

import com.webbank.model.Intervals;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IntervalsDao extends CrudRepository<Intervals, Integer> {
    @Query("select i from Intervals i where ?1 between i.dateFrom and i.leftDate and ?2 between i.rightDate and i.dateTo")
    List<Intervals> getIntervals(Date dateFrom, Date dateTo);
}
