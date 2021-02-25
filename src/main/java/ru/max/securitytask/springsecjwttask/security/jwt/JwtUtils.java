package ru.max.securitytask.springsecjwttask.security.jwt;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by maxxii on 23.02.2021.
 */
public class JwtUtils {
    public static final Set<String> blackListToken = new ConcurrentSkipListSet<>();
}
