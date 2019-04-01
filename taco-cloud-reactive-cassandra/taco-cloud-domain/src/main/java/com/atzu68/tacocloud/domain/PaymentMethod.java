package com.atzu68.tacocloud.domain;

import com.datastax.driver.core.utils.UUIDs;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Table("payment_method")
public class PaymentMethod {

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID id = UUIDs.timeBased();

    private UserUDT user;
    private String ccNumber;
    private String ccCVV;
    private String ccExpiration;

    public PaymentMethod(final UserUDT user,
                         final String ccNumber,
                         final String ccCVV,
                         final String ccExpiration) {
        this.user = user;
        this.ccNumber = ccNumber;
        this.ccCVV = ccCVV;
        this.ccExpiration = ccExpiration;
    }
}
