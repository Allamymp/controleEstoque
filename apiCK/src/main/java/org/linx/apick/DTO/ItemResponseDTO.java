package org.linx.apick.DTO;

import java.math.BigDecimal;

public record ItemResponseDTO(Long id,
                              String nome,
                              BigDecimal preco,
                              Integer quantidade,
                              BigDecimal valorTotal) {
}
