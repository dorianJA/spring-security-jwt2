package ru.max.securitytask.springsecjwttask.security.bruteforceprotect;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

/**
 * Created by maxxii on 24.02.2021.
 */
@Component
@Log
public class AuthenticationEventListenerFail implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private BruteForceService bruteForceService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent authEvent) {
        String login = authEvent.getAuthentication().getName();
        bruteForceService.registerLoginFail(login);
        log.info("Fail authentication login: "+login);
    }
}
