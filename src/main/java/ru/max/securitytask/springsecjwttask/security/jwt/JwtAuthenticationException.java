package ru.max.securitytask.springsecjwttask.security.jwt;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Created by maxxii on 22.02.2021.
 */
@Getter
public class JwtAuthenticationException extends RuntimeException {

    private HttpStatus httpStatus;
    public JwtAuthenticationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public JwtAuthenticationException(String message) {
        super(message);
    }
}
