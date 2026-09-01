package co.analisys.biblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {
    @EmbeddedId
    private PrestamoId id;

    @Embedded
    private UsuarioId usuarioId;

    @Embedded
    private LibroId libroId;

    @Embedded
    private FechaPrestamo fechaPrestamo;

    @Embedded
    private FechaDevolucionPrevista fechaDevolucionPrevista;

    @Enumerated(EnumType.STRING)
    private EstadoPrestamo estado;
}
