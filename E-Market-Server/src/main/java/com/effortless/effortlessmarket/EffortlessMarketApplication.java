package com.effortless.effortlessmarket;

import com.effortless.effortlessmarket.global.annotaion.EnableLogging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableLogging
@EnableJpaAuditing
public class EffortlessMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(EffortlessMarketApplication.class, args);
    }

}
