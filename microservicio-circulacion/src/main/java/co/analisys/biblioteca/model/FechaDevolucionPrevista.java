package co.analisys.biblioteca.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Data
@AllArgsConstructor
public class FechaDevolucionPrevista {
    private LocalDate fechadevolucionprevista_value;

    public FechaDevolucionPrevista() {
        this.fechadevolucionprevista_value = LocalDate.now().plusDays(14); // 2 semanas por defecto
    }
}
