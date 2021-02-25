package ru.max.securitytask.springsecjwttask.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by maxxii on 23.02.2021.
 */
@Getter
@Setter
public class PaymentDto {

    private String login;
    private Double payment;
}
