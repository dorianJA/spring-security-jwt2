package ru.max.securitytask.springsecjwttask.security.jwt;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by maxxii on 22.02.2021.
 */
@Component
@Log
public class JwtFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;
    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        logger.info("token filter");
        String token = jwtProvider.resolveToken(httpServletRequest);
        if(token!=null && token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        try {
            if (jwtProvider.validateToken(token) && !JwtUtils.blackListToken.contains(token)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(jwtProvider.getLoginFromToken(token));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }catch (JwtAuthenticationException | UsernameNotFoundException e){
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
