package com.atzu68.tacocloud.data;

import com.atzu68.tacocloud.domain.User;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository
        extends ReactiveCassandraRepository<User, UUID> {

    @AllowFiltering
    Mono<User> findByUsername(String username);

    @AllowFiltering
    Mono<User> findByEmail(String email);
}
