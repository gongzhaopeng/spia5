package com.atzu68.spia5.tacocloud.repository;

import com.atzu68.spia5.tacocloud.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}
