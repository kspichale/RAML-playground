package com.kspichale.rest.sample;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class SpringRestSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRestSampleApplication.class, args);
    }
}
