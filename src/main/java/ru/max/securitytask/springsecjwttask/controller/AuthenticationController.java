package ru.max.securitytask.springsecjwttask.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.max.securitytask.springsecjwttask.model.dto.AuthRequestDto;
import ru.max.securitytask.springsecjwttask.model.dto.AuthResponseDto;
import ru.max.securitytask.springsecjwttask.model.dto.PaymentDto;
import ru.max.securitytask.springsecjwttask.model.dto.RegisterRequestDto;
import ru.max.securitytask.springsecjwttask.model.entity.UserEntity;
import ru.max.securitytask.springsecjwttask.model.exception.UserLoginExistingException;
import ru.max.securitytask.springsecjwttask.security.jwt.JwtProvider;
import ru.max.securitytask.springsecjwttask.security.jwt.JwtUtils;
import ru.max.securitytask.springsecjwttask.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Created by maxxii on 22.02.2021.
 */
@RestController
@RequestMapping("/secret/api/v1")
public class AuthenticationController {

    @Value("${pay.amount}")
    private Double payment;

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public AuthenticationController(UserService userService, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        return ResponseEntity.ok(new AuthResponseDto(jwtProvider.generateToken(request.getLogin())));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDto request) {
        if (!userService.isExistLogin(request.getLogin())) {
            UserEntity userEntity = new UserEntity();
            userEntity.setPassword(request.getPassword());
            userEntity.setLogin(request.getLogin());
            userService.saveUser(userEntity);
            return ResponseEntity.ok("The user has been registered");
        }
        throw new UserLoginExistingException("The login is exists");
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
        String token = jwtProvider.resolveToken(request);
        JwtUtils.blackListToken.add(token);
    }

    @PostMapping("/payment")
    public ResponseEntity<?> pay(HttpServletRequest request) {
        String login = jwtProvider.getLoginFromToken(jwtProvider.resolveToken(request));
        userService.pay(login,payment);
        return ResponseEntity.ok("Payment completed successfully");
    }


}
