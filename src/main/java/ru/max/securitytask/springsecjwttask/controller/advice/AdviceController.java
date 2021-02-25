package ru.max.securitytask.springsecjwttask.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.max.securitytask.springsecjwttask.model.exception.NoMoneyForPaymentException;
import ru.max.securitytask.springsecjwttask.model.exception.UserLoginExistingException;
import ru.max.securitytask.springsecjwttask.security.jwt.JwtAuthenticationException;

/**
 * Created by maxxii on 22.02.2021.
 */
@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> userException(RuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<?> jwtException(RuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> authException(RuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(UserLoginExistingException.class)
    public ResponseEntity<?> loginExist(RuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body("Invalid login or password");
    }

    @ExceptionHandler(NoMoneyForPaymentException.class)
    public ResponseEntity<?> noMoney(RuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }





}
