package com.learnmicroservice.accountservice.repository;

import com.learnmicroservice.accountservice.model.StatisticDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatisticRepository extends JpaRepository<StatisticDTO , Integer> {
    List<StatisticDTO> findByStatus(boolean status);

}
