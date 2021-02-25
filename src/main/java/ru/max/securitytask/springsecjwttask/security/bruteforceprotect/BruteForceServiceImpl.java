package ru.max.securitytask.springsecjwttask.security.bruteforceprotect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.max.securitytask.springsecjwttask.model.entity.UserEntity;
import ru.max.securitytask.springsecjwttask.repository.UserRepository;

/**
 * Created by maxxii on 24.02.2021.
 */
@Component
public class BruteForceServiceImpl implements BruteForceService {

    @Autowired
    private UserRepository userService;
    @Value("${bruteforce.max_attempts}")
    private int maxFailAttempts;

    @Override
    public void registerLoginFail(String login) {
        UserEntity user = userService.findByLogin(login);
        if (user != null && !user.isLoginDisabled()) {
            if (maxFailAttempts < user.getFailedLoginAttempts() + 1) {
                user.setLoginDisabled(true);
            } else {
                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            }
            userService.save(user);
        }
    }

    @Override
    public void resetBruteForceAttempts(String login) {
        UserEntity user = userService.findByLogin(login);
        if (user != null) {
            user.setFailedLoginAttempts(0);
            user.setLoginDisabled(false);
            userService.save(user);
        }
    }
}
