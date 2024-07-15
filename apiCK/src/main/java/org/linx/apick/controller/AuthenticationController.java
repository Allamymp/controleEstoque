package org.linx.apick.controller;

import org.linx.apick.config.log.RedisLogger;
import org.linx.apick.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/authenticate")
public class AuthenticationController {
    private final RedisLogger logger;

    private final AuthenticationService authenticationService;

    public AuthenticationController(RedisLogger logger, AuthenticationService authenticationService) {
        this.logger = logger;
        this.authenticationService = authenticationService;
    }

    @PostMapping()
    public ResponseEntity<String> authenticate(Authentication authentication) {
        logger.log("info", "Request to authenticate user: " + authentication.getName());
        try {
            String response = authenticationService.authenticate(authentication);
            logger.log("info", "User authenticated successfully: " + authentication.getName());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.log("error", "Authentication failed for user: " + authentication.getName() + ". Error: " + e.getMessage());
            return ResponseEntity.status(401).body("Authentication failed.");
        }
    }
}