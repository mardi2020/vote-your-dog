package com.mardi2020.votedogvoteupdate.config;

import com.mardi2020.votedogcommon.dog.config.CommonKafkaConsumerConfig;
import com.mardi2020.votedogcommon.dog.message.DogVoteUpdate;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConsumerConfig extends CommonKafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, DogVoteUpdate> dogVoteUpdateConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupSyncConsumer");
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(DogVoteUpdate.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DogVoteUpdate> dogVoteUpdateConcurrentKafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, DogVoteUpdate> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(dogVoteUpdateConsumerFactory());
        return factory;
    }
}