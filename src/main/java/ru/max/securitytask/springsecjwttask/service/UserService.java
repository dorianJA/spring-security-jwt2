package ru.max.securitytask.springsecjwttask.service;

import ru.max.securitytask.springsecjwttask.model.entity.UserEntity;

/**
 * Created by maxxii on 22.02.2021.
 */

public interface UserService {

    UserEntity getUserByLogin(String login);

    UserEntity saveUser(UserEntity user);

    UserEntity getUserByLoginAndPass(String login, String password);

    boolean isExistLogin(String login);

    void pay(String login,Double payment);
}
