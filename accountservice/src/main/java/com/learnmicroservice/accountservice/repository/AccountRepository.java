package com.learnmicroservice.accountservice.repository;

import com.learnmicroservice.accountservice.model.AccountDTO;
import com.learnmicroservice.accountservice.model.StatisticDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountDTO, Integer> {
}
