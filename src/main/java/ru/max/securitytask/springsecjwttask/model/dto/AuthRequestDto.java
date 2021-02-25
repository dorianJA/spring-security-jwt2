package ru.max.securitytask.springsecjwttask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * Created by maxxii on 22.02.2021.
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthRequestDto {
    @NotNull(message = "Login can't be null")
    private String login;
    @NotNull(message = "Password can't be null")
    private String password;
}
