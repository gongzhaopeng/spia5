package com.atzu68.tacocloud.data;

import com.atzu68.tacocloud.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository
        extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
