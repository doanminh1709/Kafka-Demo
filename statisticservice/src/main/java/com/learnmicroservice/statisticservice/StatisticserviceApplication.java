package com.learnmicroservice.statisticservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

@SpringBootApplication
public class StatisticserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticserviceApplication.class, args);
    }


    @Bean
    JsonMessageConverter converter(){
        return new JsonMessageConverter();
    }

//    @Bean//template used to sent event that error to dlt topic
//    DefaultErrorHandler errorHandler(KafkaOperations<String , Object> template){//1000L : try sent event after 2s and sent maximum 2 times
//        return new DefaultErrorHandler(new DeadLetterPublishingRecoverer(template) , new FixedBackOff(2000L , 2));
//    }
}
