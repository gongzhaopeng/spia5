package com.atzu68.tacocloud.domain;

import org.hibernate.TypeMismatchException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;
import java.util.Optional;

public class OrderIdGenerator extends IdentityGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) {

        if (!(obj instanceof Order)) {
            throw new TypeMismatchException(
                    "Only [Order] entity is allowed.");
        }

        return Optional.<Serializable>ofNullable(((Order) obj).getId())
                .orElse(super.generate(s, obj));
    }
}
