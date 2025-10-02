package com.rezerve.rezervebookingservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic releaseSeatTopic(){
        return TopicBuilder.name("release.seat.topic.v1").build();
    }
}
