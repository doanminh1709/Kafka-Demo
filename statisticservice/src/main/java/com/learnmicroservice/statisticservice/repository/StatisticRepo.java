package com.learnmicroservice.statisticservice.repository;

import com.learnmicroservice.statisticservice.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepo extends JpaRepository<Statistic , Integer> {
}
