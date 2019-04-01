package com.atzu68.tacocloud.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@UserDefinedType("user")
public class UserUDT {

    private String username;
    private String fullname;
    private String phoneNumber;

    public static UserUDT of(User user) {

        return new UserUDT(
                user.getUsername(),
                user.getFullname(),
                user.getPhoneNumber());
    }

    public UserUDT(final String username,
                   final String fullname,
                   final String phoneNumber) {
        this.username = username;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
    }
}
