package ru.max.securitytask.springsecjwttask.security.bruteforceprotect;

/**
 * Created by maxxii on 24.02.2021.
 */
public interface BruteForceService {

    void registerLoginFail(String login);
    void resetBruteForceAttempts(String login);

}
