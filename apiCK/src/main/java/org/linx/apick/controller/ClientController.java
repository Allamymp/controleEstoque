package org.linx.apick.controller;

import jakarta.validation.Valid;
import org.linx.apick.DTO.ClientResponseDTO;
import org.linx.apick.config.log.RedisLogger;
import org.linx.apick.model.Client;
import org.linx.apick.service.ClientService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/api/client")
@CrossOrigin("*")
public class ClientController {
    private final RedisLogger logger;
    private final ClientService clientService;

    public ClientController(RedisLogger logger, ClientService clientService) {
        this.logger = logger;
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Client client) {
        logger.log("info", "Request to create new client: " + client.getEmail());
        try {
            URI location = clientService.create(client);
            logger.log("info", "Client created successfully: " + client.getEmail());
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            logger.log("error", "Error creating client: " + client.getEmail() + "\nException: " + e.getMessage());
            throw new RuntimeException("Error creating client", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable Long id) {
        logger.log("info", "Request to find client by ID: " + id);
        try {
            ResponseEntity<ClientResponseDTO> response = clientService.findById(id);
            logResponseStatus(response.getStatusCode(), String.valueOf(id));
            return response;
        } catch (Exception e) {
            logger.log("error", "Error finding client by ID: " + id + "\nException: " + e.getMessage());
            throw new RuntimeException("Error finding client by ID", e);
        }
    }

    @GetMapping("/email")
    public ResponseEntity<ClientResponseDTO> findByEmail(@RequestParam String email) {
        logger.log("info", "Request to find client by email: " + email);
        try {
            ResponseEntity<ClientResponseDTO> response = clientService.findByEmail(email);
            logResponseStatus(response.getStatusCode(), email);
            return response;
        } catch (Exception e) {
            logger.log("error", "Error finding client by email: " + email + "\nException: " + e.getMessage());
            throw new RuntimeException("Error finding client by email", e);
        }
    }

    private void logResponseStatus(HttpStatusCode status, String identifier) {
        if (status.is2xxSuccessful()) {
            logger.log("info", "Client found: " + identifier);
        } else {
            logger.log("warn", "Client not found: " + identifier);
        }
    }
}
