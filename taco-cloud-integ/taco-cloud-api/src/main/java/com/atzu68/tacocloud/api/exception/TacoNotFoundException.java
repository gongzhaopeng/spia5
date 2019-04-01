package com.atzu68.tacocloud.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.NOT_FOUND,
        reason = "Taco Not Found")
public class TacoNotFoundException
        extends RuntimeException {
}
