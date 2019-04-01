package com.atzu68.spia5.greetings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "greeting")
@Data
public class GreetingProps {

    private String message;
}
