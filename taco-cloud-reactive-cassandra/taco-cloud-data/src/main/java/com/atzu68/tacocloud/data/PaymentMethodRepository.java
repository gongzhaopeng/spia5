package com.atzu68.tacocloud.data;

import com.atzu68.tacocloud.domain.PaymentMethod;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentMethodRepository
        extends ReactiveCrudRepository<PaymentMethod, UUID> {

    @AllowFiltering
    Mono<PaymentMethod> findByUserUsername(String username);
}
