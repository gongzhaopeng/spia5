package com.atzu68.spia5.integration.simpleintegflow;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SimpleIntegFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleIntegFlowApplication.class, args);
    }

    @Bean
    public CommandLineRunner writeData(
            FileWriterGateway fileWriterGateway) {

        return args -> {

            fileWriterGateway.writeToFile(
                    "simple.txt",
                    "Hello, Spring Integration!");
        };
    }

}

