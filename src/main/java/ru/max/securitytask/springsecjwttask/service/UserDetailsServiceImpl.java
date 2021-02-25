package ru.max.securitytask.springsecjwttask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.max.securitytask.springsecjwttask.model.entity.UserEntity;

import java.util.Collections;

/**
 * Created by maxxii on 22.02.2021.
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getUserByLogin(login);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(userEntity.getLogin(),userEntity.getPassword(),
                true,true,true,
                !userEntity.isLoginDisabled(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
