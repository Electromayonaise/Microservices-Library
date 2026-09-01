package co.analisys.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credenciales {

    private String username;
    private String passwordHash;
    // métodos para verificar credenciales
}
