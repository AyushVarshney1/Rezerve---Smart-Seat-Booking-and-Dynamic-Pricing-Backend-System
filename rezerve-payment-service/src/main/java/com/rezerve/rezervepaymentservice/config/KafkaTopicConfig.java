package com.rezerve.rezervepaymentservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaTopicConfig {

    @Bean
    public NewTopic paymentSuccessfulTopic(){
        return TopicBuilder.name("payment.successful.topic.v1").build();
    }

    @Bean
    public NewTopic paymentSuccessfulNotificationTopic(){
        return TopicBuilder.name("payment.successful.notification.topic.v1").build();
    }

    @Bean
    public NewTopic paymentFailedNotificationTopic(){
        return TopicBuilder.name("payment.failed.notification.topic.v1").build();
    }
}
