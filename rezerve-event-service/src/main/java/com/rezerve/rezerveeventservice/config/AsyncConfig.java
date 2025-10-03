package com.rezerve.rezerveeventservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "KafkaExecutor")
    public Executor kafkaExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // THE CORE THREADS WHICH WILL ALWAYS BE KEPT ALIVE
        executor.setCorePoolSize(5);
        // THE MAX NUMBER OF THREADS THE WILL BE CREATED IF CORE THREADS ARE BUSY
        executor.setMaxPoolSize(10);
        // QUEUE SIZE FOR TASKS WAITING FOR A THREAD
        executor.setQueueCapacity(100);
        // PREFIX USED TO IDENTIFY THREAD AND MAKE DEBUGGING EASIER
        executor.setThreadNamePrefix("Kafka-Executor-");

        executor.initialize();
        return executor;
    }
}
