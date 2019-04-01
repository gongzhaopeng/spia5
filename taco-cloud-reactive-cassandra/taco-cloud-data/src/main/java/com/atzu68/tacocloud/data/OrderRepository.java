package com.atzu68.tacocloud.data;

import com.atzu68.tacocloud.domain.Order;
import com.atzu68.tacocloud.domain.UserUDT;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface OrderRepository
        extends ReactiveCassandraRepository<Order, UUID> {

    @AllowFiltering
    Flux<Order> findByUser(UserUDT user);
}
