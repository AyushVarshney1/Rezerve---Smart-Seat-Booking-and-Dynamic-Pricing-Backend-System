package com.rezerve.rezerveeventservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RezerveEventServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RezerveEventServiceApplication.class, args);
    }

}
