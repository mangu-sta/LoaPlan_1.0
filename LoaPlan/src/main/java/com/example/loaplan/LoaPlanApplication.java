package com.example.loaplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoaPlanApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoaPlanApplication.class, args);
    }

}
