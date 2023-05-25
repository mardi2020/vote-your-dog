package com.mardi2020.votedogvoteupdate.Dog.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mardi2020.votedogcommon.Dog.Message.DogParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DogKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper;

    public void send(String topic, DogParam data) {
        String message;
        try {
            message = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        kafkaTemplate.send(topic, message);
        log.info("Kafka produces message: " + message);
    }
}
