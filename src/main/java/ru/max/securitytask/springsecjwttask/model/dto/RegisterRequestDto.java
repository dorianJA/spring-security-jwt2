package ru.max.securitytask.springsecjwttask.model.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * Created by maxxii on 22.02.2021.
 */
@Getter
@Setter
@AllArgsConstructor
public class RegisterRequestDto {
    @NotNull(message = "Login can't be null")
    @NotBlank(message = "Login can't be empty")
    private String login;
    @NotNull(message = "Password can't be null")
    @NotBlank(message = "Password can't be empty")
    private String password;
}
