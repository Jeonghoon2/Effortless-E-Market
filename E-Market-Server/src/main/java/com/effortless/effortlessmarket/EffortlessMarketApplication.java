package com.effortless.effortlessmarket;

import com.effortless.effortlessmarket.annotation.EnableLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableLogging
public class EffortlessMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(EffortlessMarketApplication.class, args);
    }

}
