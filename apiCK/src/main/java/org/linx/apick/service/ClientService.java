package org.linx.apick.service;

import org.linx.apick.DTO.ClientResponseDTO;
import org.linx.apick.config.log.RedisLogger;
import org.linx.apick.exception.DuplicatedEmailException;
import org.linx.apick.model.Client;
import org.linx.apick.repository.ClientRepository;
import org.linx.apick.util.DTOFactory;
import org.linx.apick.util.ValidatePassword;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final RedisLogger logger;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, RedisLogger logger, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.logger = logger;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public URI create(Client data) {
        String email = data.getEmail();
        if (clientRepository.findByEmail(email).isPresent()) {
            logger.log("info", "ClientService: User with email " + email + " already exists in database.");
            throw new DuplicatedEmailException("Email already exists: " + email);
        }
        
        logger.log("info", "Creating new client with email: " + email);

        data.setPassword(passwordEncoder.encode(data.getPassword()));
        Client client = clientRepository.save(data);

        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getId())
                .toUri();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ClientResponseDTO> findById(Long id) {
        logger.log("info", "Searching for client with ID: " + id);
        try {
        } catch (NumberFormatException e) {
            logger.log("error", "Invalid ID format: " + id);
            return ResponseEntity.badRequest().build();
        }
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            logger.log("info", "Client found for id " + id + ": " + client.get().getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(DTOFactory.createClientReturnDTO(client.get()));
        } else {
            logger.log("info", "Client not found for id: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ClientResponseDTO> findByEmail(String email) {
        logger.log("info", "Searching for client with email: " + email);
        Optional<Client> client = clientRepository.findByEmail(email);
        if (client.isPresent()) {
            logger.log("info", "Client found for email " + email + ": " + client.get().getName());
            return ResponseEntity.status(HttpStatus.OK).body(DTOFactory.createClientReturnDTO(
                    client.get()));
        } else {
            logger.log("info", "Client not found for email: " + email);
            return ResponseEntity.notFound().build();
        }
    }

    // Outros métodos foram propositalmente não implementados por segurança.

}
