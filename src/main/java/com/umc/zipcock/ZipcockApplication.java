package com.umc.zipcock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class ZipcockApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZipcockApplication.class, args);
    }

}
