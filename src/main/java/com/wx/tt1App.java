package com.wx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.wx")
@EnableAutoConfiguration
public class tt1App {
    public static void main(String[] args) {
        SpringApplication.run(tt1App.class, args);
    }
}
