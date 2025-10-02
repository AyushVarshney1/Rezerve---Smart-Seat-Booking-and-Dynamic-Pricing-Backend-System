package com.rezerve.rezervepricingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RezervePricingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RezervePricingServiceApplication.class, args);
    }

}
