package com.atzu68.spia5.eureka.client.ingredient.resttemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Conditional(NotFeignAndNotWebClientCondition.class)
@Configuration
@Slf4j
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner startup() {
        return args -> {

            log.info("**************************************");
            log.info("    Configuring with RestTemplate");
            log.info("**************************************");
        };
    }
}
