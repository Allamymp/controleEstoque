package org.linx.apick.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "TB_ITEM")
@Getter
@Setter
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 50, message = "Max character name is 50.")
    @Column(unique = true)
    private String name;

    @NotNull(message = "Price can't be null")
    private BigDecimal price;

    @NotNull(message = "Item quantity is mandatory")
    private Integer quantity;

    public Item(String name, BigDecimal price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        if (this.price != null && this.quantity != null) {
            return this.price.multiply(BigDecimal.valueOf(this.quantity));
        }
        return BigDecimal.ZERO;
    }
}
