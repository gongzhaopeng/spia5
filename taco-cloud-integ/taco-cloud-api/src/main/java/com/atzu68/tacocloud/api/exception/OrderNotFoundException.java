package com.atzu68.tacocloud.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        code = HttpStatus.NOT_FOUND,
        reason = "Order Not Found")
public class OrderNotFoundException
        extends RuntimeException {
}
