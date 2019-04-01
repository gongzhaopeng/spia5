package com.atzu68.spia5.tacocloud.repository;

import com.atzu68.spia5.tacocloud.model.Order;
import com.atzu68.spia5.tacocloud.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository
        extends CrudRepository<Order, Long> {

    List<Order> findByUserOrderByPlacedAtDesc(
            User user, Pageable pageable);
}
