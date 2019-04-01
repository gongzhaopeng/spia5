package com.atzu68.tacocloud.data;

import com.atzu68.tacocloud.domain.Taco;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import java.util.UUID;

public interface TacoRepository
        extends ReactiveCassandraRepository<Taco, UUID> {
}
