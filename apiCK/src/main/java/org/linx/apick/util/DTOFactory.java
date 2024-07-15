package org.linx.apick.util;

import org.linx.apick.DTO.ClientResponseDTO;
import org.linx.apick.DTO.ItemResponseDTO;
import org.linx.apick.model.Client;
import org.linx.apick.model.Item;

public class DTOFactory {

    public static ClientResponseDTO createClientReturnDTO(Client client) {
        return new ClientResponseDTO(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getRole().toString()
        );
    }

    public static ItemResponseDTO createItemReturnDTO( Item item) {
        return new ItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getQuantity(),
                item.getTotalPrice()
        );
    }
}
