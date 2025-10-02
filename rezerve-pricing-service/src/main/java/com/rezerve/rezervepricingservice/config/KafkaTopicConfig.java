package com.rezerve.rezervepricingservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic eventPriceUpdatedTopic(){
        return TopicBuilder.name("event.price.updated.topic.v1").build();
    }
}
