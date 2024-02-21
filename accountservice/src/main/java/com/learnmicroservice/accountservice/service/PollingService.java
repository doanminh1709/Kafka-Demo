package com.learnmicroservice.accountservice.service;

import com.learnmicroservice.accountservice.model.MessageDTO;
import com.learnmicroservice.accountservice.model.StatisticDTO;
import com.learnmicroservice.accountservice.repository.AccountRepository;
import com.learnmicroservice.accountservice.repository.MessageRepository;
import com.learnmicroservice.accountservice.repository.StatisticRepository;
import io.micrometer.common.lang.Nullable;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.CompletableToListenableFutureAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class PollingService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    StatisticRepository statisticRepository;
    @Scheduled(fixedDelay = 1000)
    public void sendingMessage(){
        List<MessageDTO> messageDTOList = messageRepository.findByStatus(false);
        for (MessageDTO messageDTO : messageDTOList){
            /*Todo :
               - CompletableFuture<SendResult<K , V>> completableFuture returned by kafkaTemplate.send()
               - ListenableFuture<SendResult<K,V>> future is created by adapting the CompletableFuture
                 using CompletableFutureToListenableFutureAdapter
               - future.addCallback(...)  attaches a `ListenableFutureCallback` to handle success and failure cases.
             */
            CompletableFuture<SendResult<String, Object>> completableFuture  =  kafkaTemplate.send("notification", messageDTO);
            ListenableFuture<SendResult<String, Object>> future = new CompletableToListenableFutureAdapter<>(completableFuture);
            future.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onSuccess(final SendResult<String, Object> result) {
                    logger.info("SUCCESS" );
                    messageDTO.setStatus(true);
                    messageRepository.save(messageDTO);
                }
                @Override
                public void onFailure(final Throwable throwable) {
                    logger.error("unable to send message= " + throwable.getMessage());
                }
            });
        }

        List<StatisticDTO> statisticDTOS = statisticRepository.findByStatus(false);
        for (StatisticDTO statisticDTO : statisticDTOS){
            CompletableFuture<SendResult<String, Object>> completableFuture  =  kafkaTemplate.send("statistic", statisticDTO);
            ListenableFuture<SendResult<String, Object>> future = new CompletableToListenableFutureAdapter<>(completableFuture);
            future.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onSuccess(final SendResult<String, Object> result) {
                    logger.info("SUCCESS");
                    statisticDTO.setStatus(true);
                    statisticRepository.save(statisticDTO);
                }
                @Override
                public void onFailure(final Throwable throwable) {
                    logger.error("unable to send message= " + throwable.getMessage());
                }
            });
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void deleteMessage(){
        List<MessageDTO> messageDTOS = messageRepository.findByStatus(true);
        messageRepository.deleteAllInBatch(messageDTOS);

        List<StatisticDTO> statisticDTOS = statisticRepository.findByStatus(true);
        statisticRepository.deleteAllInBatch(statisticDTOS);
    }
}
