package org.linx.apick.service;


import org.linx.apick.DTO.ItemResponseDTO;
import org.linx.apick.config.log.RedisLogger;
import org.linx.apick.exception.DuplicatedItemNameException;
import org.linx.apick.model.Item;
import org.linx.apick.repository.ItemRepository;
import org.linx.apick.util.DTOFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final RedisLogger logger;

    public ItemService(ItemRepository itemRepository, RedisLogger logger) {
        this.itemRepository = itemRepository;
        this.logger = logger;
    }

    @Transactional
    public URI create(Item data) {
        try {
            if (itemRepository.findByName(data.getName()).isPresent()) {
                logger.log("info", "ItemService: Item with name " + data.getName() + " already exists in database.");
                throw new DuplicatedItemNameException("Item with name already exists: " + data.getName());
            }
            logger.log("info", "Creating new item with name: " + data.getName());

            Item item = itemRepository.save(data);
            return ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(item.getId())
                    .toUri();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional(readOnly = true)
    public ResponseEntity<ItemResponseDTO> findById(Long id) {
        logger.log("info", "Searching for item with ID: " + id);

        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            logger.log("info", "Item found for id " + id + ": " + item.get().getName());
            return ResponseEntity.status(HttpStatus.OK).body(DTOFactory.createItemReturnDTO(item.get()));
        } else {
            logger.log("info", "Item not found for id: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ItemResponseDTO> findByName(String name) {
        logger.log("info", "Searching for item with name: " + name);
        Optional<Item> item = itemRepository.findByName(name);
        if (item.isPresent()) {
            logger.log("info", "Item found for item " + item + ": " + item.get().getName());
            return ResponseEntity.status(HttpStatus.OK).body(DTOFactory.createItemReturnDTO(item.get()
            ));
        } else {
            logger.log("info", "Item not found for name: " + name);
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<?> changeItemQuantity(Integer quantity, Long id) {
        logger.log("info", "Updating item quantity information for item with ID: " + id);
        Optional<Item> item = itemRepository.findById(id);
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }

        if (item.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Item concreteItem = item.get();
            if (!concreteItem.getQuantity().equals(quantity)) {
                concreteItem.setQuantity(quantity);
                itemRepository.save(concreteItem);
            }
            return ResponseEntity.ok().build();
        }
    }


    @Transactional
    public ResponseEntity<?> changeItemPrice(BigDecimal price, Long id) {
        logger.log("info", "Updating item price information for item with ID: " + id);
        Optional<Item> item = itemRepository.findById(id);
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        if (item.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Item concreteItem = item.get();
            if (!concreteItem.getPrice().equals(price)) {
                concreteItem.setPrice(price);
                itemRepository.save(concreteItem);
            }
            return ResponseEntity.ok().build();
        }
    }

    @Transactional(readOnly = true)
    public List<ItemResponseDTO> all() {
        logger.log("info", "Finding all items");


        List<Item> list = itemRepository.findAll();


        return list.stream()
                .map(DTOFactory::createItemReturnDTO)
                .collect(Collectors.toList());    }

}
