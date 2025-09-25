package com.rezerve.rezervepaymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RezervePaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RezervePaymentServiceApplication.class, args);
    }

}
