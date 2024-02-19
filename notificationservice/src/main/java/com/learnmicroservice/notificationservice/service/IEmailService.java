package com.learnmicroservice.notificationservice.service;

import com.learnmicroservice.notificationservice.model.MessageDTO;

public interface IEmailService {
    void sendMail(MessageDTO messageDTO);
}