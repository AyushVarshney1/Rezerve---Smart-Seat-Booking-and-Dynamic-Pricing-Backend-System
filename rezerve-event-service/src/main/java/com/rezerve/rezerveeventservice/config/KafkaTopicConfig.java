package com.rezerve.rezerveeventservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic eventCreatedTopic(){
        return TopicBuilder.name("event.created.topic.v1").build();
    }

    @Bean
    public NewTopic eventSeatsUpdatedTopic(){
        return TopicBuilder.name("event.seats.updated.topic.v1").build();
    }

    @Bean
    public NewTopic eventDeletedTopic(){
        return TopicBuilder.name("event.deleted.topic.v1").build();
    }
}
