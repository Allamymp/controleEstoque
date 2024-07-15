package org.linx.apick.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("Admin"),
    FUNCIONARIO("Funcionario"),
    SUPERVISOR("Supervisor"),
    GERENTE("Gerente");

    private final String role;
}
