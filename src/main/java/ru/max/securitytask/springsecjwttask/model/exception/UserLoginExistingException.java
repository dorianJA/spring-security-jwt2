package ru.max.securitytask.springsecjwttask.model.exception;

/**
 * Created by maxxii on 23.02.2021.
 */
public class UserLoginExistingException extends RuntimeException {
    public UserLoginExistingException(String message) {
        super(message);
    }
}
