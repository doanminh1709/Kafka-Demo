package com.learnmicroservice.accountservice.controller;

import com.learnmicroservice.accountservice.model.AccountDTO;
import com.learnmicroservice.accountservice.model.MessageDTO;
import com.learnmicroservice.accountservice.model.StatisticDTO;
import com.learnmicroservice.accountservice.repository.AccountRepository;
import com.learnmicroservice.accountservice.repository.MessageRepository;
import com.learnmicroservice.accountservice.repository.StatisticRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.lang.Nullable;
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

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    StatisticRepository statisticRepository;

    @PostMapping("/new")
    public AccountDTO create(@RequestBody AccountDTO accountDTO){
        StatisticDTO statisticDTO = new StatisticDTO("Account " + accountDTO.getEmail() + "is created ", new Date());
        statisticDTO.setStatus(false);
        //send notification
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTo(accountDTO.getEmail());
        messageDTO.setToName(accountDTO.getName());
        messageDTO.setSubject("Welcome to my home");
        messageDTO.setContent("My home is most cosy place!");
        messageDTO.setStatus(false);

        accountRepository.save(accountDTO);
        messageRepository.save(messageDTO);
        statisticRepository.save(statisticDTO);

        return accountDTO;
    }
}
