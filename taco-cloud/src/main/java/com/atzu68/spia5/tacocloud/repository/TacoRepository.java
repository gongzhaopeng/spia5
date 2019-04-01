package com.atzu68.spia5.tacocloud.repository;

import com.atzu68.spia5.tacocloud.model.Taco;
import org.springframework.data.repository.CrudRepository;

public interface TacoRepository
        extends CrudRepository<Taco, Long> {
}
