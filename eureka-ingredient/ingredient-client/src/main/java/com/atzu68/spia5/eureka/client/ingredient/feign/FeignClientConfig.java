package com.atzu68.spia5.eureka.client.ingredient.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("feign")
@Configuration
@Slf4j
@EnableFeignClients
public class FeignClientConfig {

    @Bean
    public CommandLineRunner startup() {
        return args -> {

            log.info("**************************************");
            log.info("        Configuring with Feign");
            log.info("**************************************");
        };
    }
}
