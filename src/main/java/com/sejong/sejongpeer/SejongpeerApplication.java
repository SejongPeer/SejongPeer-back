package com.sejong.sejongpeer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SejongpeerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SejongpeerApplication.class, args);
    }
}
