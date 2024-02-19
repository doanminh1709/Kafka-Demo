package com.learnmicroservice.accountservice.controller;

import com.learnmicroservice.accountservice.model.AccountDTO;
import com.learnmicroservice.accountservice.model.MessageDTO;
import com.learnmicroservice.accountservice.model.StatisticDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/new")
    public AccountDTO create(@RequestBody AccountDTO accountDTO){
        StatisticDTO statisticDTO = new StatisticDTO("Account " + accountDTO.getEmail() + "is created ", new Date());
        //send notification
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTo(accountDTO.getEmail());
        messageDTO.setToName(accountDTO.getName());
        messageDTO.setSubject("Welcome to my home");
        messageDTO.setContent("My home is most cosy place!");
        kafkaTemplate.send("notification" , messageDTO);
        kafkaTemplate.send("statistic" , statisticDTO);
        return accountDTO;
    }
}
