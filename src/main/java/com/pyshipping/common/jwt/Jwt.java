package com.pyshipping.common.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pyshipping.config.JwtConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Slf4j
@Component
public class Jwt {

    @Autowired
    private JwtConfig jwtConfig;
    public String generateJwt(String userID, String username,String password,Integer status) {
        Date date = new Date(System.currentTimeMillis() + 86400L * 1000 * jwtConfig.getExpireday());
        JwtBody jwtBody = new JwtBody(userID,username,password,status);
        String body = jwtBody.toString();
        String token = JWT.create()
                .withClaim("employee",body)
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256(jwtConfig.getJwtkey()));
        return "Bearer " + token;

    }

    public JwtBody verifyJwt(String bearerToken) {
        String token = bearerToken.split(" ")[1];
        DecodedJWT dj = JWT.require(Algorithm.HMAC256(jwtConfig.getJwtkey())).build().verify(token);
        String body = dj.getClaim("employee").asString();
        return JwtBody.toJwtBody(body);
    }

    public boolean removeJwt(String bearerToken) {
        String token = bearerToken.split(" ")[1];
        DecodedJWT dj = JWT.require(Algorithm.HMAC256(jwtConfig.getJwtkey())).build().verify(token);
        String asBody = dj.getClaim("employee").asString();
        JwtBody jwtBody = JwtBody.toJwtBody(asBody);
        Date date = new Date(System.currentTimeMillis() * 20);
        String body = jwtBody.toString();
        String newToken = JWT.create()
                .withClaim("user", body)
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256(jwtConfig.getJwtkey()));
        return newToken != null;
    }
}
