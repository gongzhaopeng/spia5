package com.atzu68.tacocloud.data;

import com.atzu68.tacocloud.domain.Taco;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TacoRepository
        extends PagingAndSortingRepository<Taco, Long> {
}
