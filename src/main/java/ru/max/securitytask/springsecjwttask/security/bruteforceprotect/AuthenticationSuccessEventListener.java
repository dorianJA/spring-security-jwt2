package ru.max.securitytask.springsecjwttask.security.bruteforceprotect;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * Created by maxxii on 24.02.2021.
 */
@Component
@Log
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private BruteForceService bruteForceService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent authEvent) {
        String login = authEvent.getAuthentication().getName();
        bruteForceService.resetBruteForceAttempts(login);
        log.info("Authentication success login: "+login);
    }
}
