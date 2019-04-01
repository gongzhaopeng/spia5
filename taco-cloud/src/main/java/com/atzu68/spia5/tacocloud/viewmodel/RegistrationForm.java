package com.atzu68.spia5.tacocloud.viewmodel;

import lombok.Data;
import com.atzu68.spia5.tacocloud.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;

@Data
public class RegistrationForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String fullname;

    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;

    public User toUser(PasswordEncoder passwordEncoder) {

        return new User(
                username, passwordEncoder.encode(password),
                fullname, street, city, state, zip, phone);
    }
}
