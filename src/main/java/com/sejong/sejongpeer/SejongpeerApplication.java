package com.sejong.sejongpeer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class SejongpeerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SejongpeerApplication.class, args);
    }
}
