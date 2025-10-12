package com.rezerve.rezervebookingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class RezerveBookingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RezerveBookingServiceApplication.class, args);
    }

}
