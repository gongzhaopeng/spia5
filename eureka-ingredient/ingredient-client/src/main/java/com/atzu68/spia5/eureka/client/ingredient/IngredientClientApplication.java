package com.atzu68.spia5.eureka.client.ingredient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class IngredientClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(IngredientClientApplication.class, args);
    }

    @Bean
    public CommandLineRunner launch(MongoConfig mongoConfig) {

        return args -> {

            log.info(
                    "The username of mongodb is {}.",
                    mongoConfig.getUsername());

            log.info(
                    "The password of mongodb is {}.",
                    mongoConfig.getPassword());
        };
    }
}

