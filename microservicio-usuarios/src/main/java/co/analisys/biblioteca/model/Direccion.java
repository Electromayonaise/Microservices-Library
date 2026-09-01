package co.analisys.biblioteca.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    private String calle;
    private String ciudad;
    private String codigoPostal;
    // getters y setters
}
