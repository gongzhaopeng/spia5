package com.atzu68.spia5.eureka.client.ingredient.resttemplate;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Profiles;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class NotFeignAndNotWebClientCondition implements Condition {

    @Override
    public boolean matches(
            ConditionContext conditionContext,
            AnnotatedTypeMetadata annotatedTypeMetadata) {

        return !conditionContext.getEnvironment()
                .acceptsProfiles(
                        Profiles.of(
                                "feign",
                                "webclient"
                        )
                );
    }
}
