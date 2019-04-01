package com.atzu68.spia5.eureka.client.ingredient;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Data
public class MongoConfig {

    private String username;    // Stored in the Vault server.
    private String password;
}
