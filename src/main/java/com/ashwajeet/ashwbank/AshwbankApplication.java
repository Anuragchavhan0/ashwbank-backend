package com.ashwajeet.ashwbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class AshwbankApplication {
    public static void main(String[] args) {
        SpringApplication.run(AshwbankApplication.class, args);
    }
}
