package co.analisys.biblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {

    @EmbeddedId
    private UsuarioId id;

    @Column(name = "nombre")
    private String nombre;

    @Embedded
    private Email email;

    @Embedded
    private Direccion direccion;

    @Embedded
    private Credenciales credenciales;

    public void cambiarEmail(Email nuevoEmail) {
        this.email = nuevoEmail;
    }

    public void actualizarDireccion(Direccion nuevaDireccion) {
        this.direccion = nuevaDireccion;
    }
}
