package org.linx.apick.service;

import org.linx.apick.config.log.RedisLogger;
import org.linx.apick.security.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final RedisLogger logger;
    private final JwtService jwtService;


    public AuthenticationService(RedisLogger logger, JwtService jwtService) {
        this.logger = logger;
        this.jwtService = jwtService;
    }

    public String authenticate(Authentication authentication) {
        logger.log("info", "Authenticating user: " + authentication.getName());
        String token = jwtService.generateToken(authentication);
        logger.log("info", "User authenticated successfully: " + authentication.getName());
        return token;
    }

}