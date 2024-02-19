package com.learnmicroservice.notificationservice.service;

import com.learnmicroservice.notificationservice.model.MessageDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements IEmailService{

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired//read email template
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    @Async
    public void sendMail(MessageDTO messageDTO) {
        try{
            logger.info("START ... Sending email");
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message , StandardCharsets.UTF_8.name());

            //load template email with content
            Context context = new Context();
            context.setVariable("name" , messageDTO.getToName());
            context.setVariable("content" , messageDTO.getContent());
            String html = templateEngine.process("welcome-email" , context);

            //send mail
            helper.setTo(messageDTO.getTo());
            helper.setText(html , true);
            helper.setFrom(from);
            javaMailSender.send(message);

            logger.info("END... Email sent success!");
        }catch (MessagingException exception){
            logger.error("Email sent with error :" + exception.getMessage());
        }
    }
}
