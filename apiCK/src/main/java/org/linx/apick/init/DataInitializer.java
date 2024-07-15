package org.linx.apick.init;

import jakarta.annotation.PostConstruct;
import org.linx.apick.enums.Role;
import org.linx.apick.model.Client;
import org.linx.apick.model.Item;
import org.linx.apick.repository.ClientRepository;
import org.linx.apick.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class DataInitializer {
    private final ClientRepository clientRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(ClientRepository clientRepository, ItemRepository itemRepository, PasswordEncoder passwordEncoder) {

        this.clientRepository = clientRepository;
        this.itemRepository = itemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void initializeData() {
        if (clientRepository.findByEmail("admin@example.com").isEmpty()) {
            Client adminClient = new Client();
            adminClient.setName("Carla Maria");
            adminClient.setEmail("admin@example.com");
            adminClient.setPassword(passwordEncoder.encode("senha"));
            adminClient.setRole(Role.ADMIN); // Assuming Role is an enum with a valueOf method

            clientRepository.save(adminClient);
        }

        initializeCalvinKleinItems();
    }

    private void initializeCalvinKleinItems() {
        initializeItem("Calvin Klein Mens Jeans", 149.99, 5);
        initializeItem("Calvin Klein Womens Dress", 199.99, 3);
        initializeItem("Calvin Klein Mens T-Shirt", 59.99, 7);
        initializeItem("Calvin Klein Womens Shoes", 129.99, 4);
        initializeItem("Calvin Klein Mens Watch", 299.99, 2);
        initializeItem("Calvin Klein Womens Handbag", 179.99, 3);
        initializeItem("Calvin Klein Mens Belt", 49.99, 10);
        initializeItem("Calvin Klein Womens Perfume", 89.99, 8);
        initializeItem("Calvin Klein Mens Socks", 19.99, 20);
        initializeItem("Calvin Klein Womens Sunglasses", 119.99, 5);
    }

    public void initializeItem(String name, double price, int quantity) {
        Optional<Item> existingItem = itemRepository.findByName(name);

        if (existingItem.isEmpty()) {
            Item item = new Item();
            item.setName(name);
            item.setPrice(BigDecimal.valueOf(price));
            item.setQuantity(quantity);

            itemRepository.save(item);
        }
    }
}
