package ru.max.securitytask.springsecjwttask.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Created by maxxii on 22.02.2021.
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_login")
    private String login;
    @Column(name = "user_password")
    private String password;
    @Column(name = "user_balance",precision = 20,scale = 2)
    private Double balance;

    @OneToMany(mappedBy = "user")
    private List<Payment> payments;

    @Column(name = "login_fail_attempts")
    private int failedLoginAttempts;
    @Column(name = "login_is_disabled")
    private boolean loginDisabled;

}
