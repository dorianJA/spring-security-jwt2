package ru.max.securitytask.springsecjwttask.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

/**
 * Created by maxxii on 22.02.2021.
 */
@Component
public class JwtProvider {

    @Value("${jwt.expire_date_day}")
    private int expirationDate;
    @Value("${jwt.secret_key}")
    private String secretKey;
    @Value("${jwt.header}")
    private String authorization;




    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateToken(String login) {
        Date expDate = Date.from(LocalDate.now().plusDays(expirationDate)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date now =  new Date();


        return Jwts.builder()
                .setSubject(login)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JW token is expired or invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    public String getLoginFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


    public String resolveToken(HttpServletRequest request){
        return request.getHeader(authorization);
    }


}
