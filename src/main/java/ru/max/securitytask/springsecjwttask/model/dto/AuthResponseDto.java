package ru.max.securitytask.springsecjwttask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by maxxii on 22.02.2021.
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
}
