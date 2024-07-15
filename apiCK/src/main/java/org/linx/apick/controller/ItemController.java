package org.linx.apick.controller;

import jakarta.validation.Valid;
import org.linx.apick.DTO.ItemResponseDTO;
import org.linx.apick.config.log.RedisLogger;
import org.linx.apick.model.Item;
import org.linx.apick.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/api/item")
@CrossOrigin("*")
public class ItemController {

    private final RedisLogger logger;
    private final ItemService itemService;

    public ItemController(RedisLogger logger, ItemService itemService) {
        this.logger = logger;
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Item item) {
        logger.log("info", "Request to create new item: " + item.getName());
        try {
            URI location = itemService.create(item);
            logger.log("info", "Item created successfully: " + item.getName());
            return ResponseEntity.created(location).build();
        } catch (Exception e) {
            logger.log("error", "Error creating item: " + item.getName() + "\nException: " + e.getMessage());
            throw new RuntimeException("Error creating item", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> findById(@PathVariable Long id) {
        logger.log("info", "Request to find item by ID: " + id);
        try {
            ResponseEntity<ItemResponseDTO> response = itemService.findById(id);
            logResponseStatus(response.getStatusCode(), String.valueOf(id));
            return response;
        } catch (Exception e) {
            logger.log("error", "Error finding item by ID: " + id + "\nException: " + e.getMessage());
            throw new RuntimeException("Error finding item by ID", e);
        }
    }

    @GetMapping("/name")
    public ResponseEntity<ItemResponseDTO> findByName(@RequestParam String name) {
        logger.log("info", "Request to find item by name: " + name);
        try {
            ResponseEntity<ItemResponseDTO> response = itemService.findByName(name);
            logResponseStatus(response.getStatusCode(), name);
            return response;
        } catch (Exception e) {
            logger.log("error", "Error finding item by name: " + name + "\nException: " + e.getMessage());
            throw new RuntimeException("Error finding item by name", e);
        }
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<?> changeItemQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        logger.log("info", "Request to change item quantity for item with ID: " + id);
        try {
            ResponseEntity<?> response = itemService.changeItemQuantity(quantity, id);
            logResponseStatus(response.getStatusCode(), String.valueOf(id));
            return response;
        } catch (Exception e) {
            logger.log("error", "Error changing item quantity for item with ID: " + id + "\nException: " + e.getMessage());
            throw new RuntimeException("Error changing item quantity", e);
        }
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<?> changeItemPrice(@PathVariable Long id, @RequestParam BigDecimal price) {
        logger.log("info", "Request to change item price for item with ID: " + id);
        try {
            ResponseEntity<?> response = itemService.changeItemPrice(price, id);
            logResponseStatus(response.getStatusCode(), String.valueOf(id));
            return response;
        } catch (Exception e) {
            logger.log("error", "Error changing item price for item with ID: " + id + "\nException: " + e.getMessage());
            throw new RuntimeException("Error changing item price", e);
        }
    }

    @GetMapping("/all")
    public List<ItemResponseDTO> all() {
        logger.log("info", "Request to fetch all items");
        return itemService.all();
    }

    private void logResponseStatus(HttpStatusCode status, String identifier) {
        if (status.is2xxSuccessful()) {
            logger.log("info", "Operation successful for: " + identifier);
        } else {
            logger.log("warn", "Operation failed for: " + identifier);
        }
    }
}
