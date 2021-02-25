package ru.max.securitytask.springsecjwttask.model.exception;

/**
 * Created by maxxii on 23.02.2021.
 */
public class NoMoneyForPaymentException extends RuntimeException {
    public NoMoneyForPaymentException(String message) {
        super(message);
    }
}
