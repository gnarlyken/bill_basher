package com.billbasher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })

public class BillBasherApplication{

    public static void main(String[] args) {
        SpringApplication.run(BillBasherApplication.class, args);
    }
}
