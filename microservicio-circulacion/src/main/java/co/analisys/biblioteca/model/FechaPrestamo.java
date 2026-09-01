package co.analisys.biblioteca.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Data
@AllArgsConstructor
public class FechaPrestamo {
    private LocalDate fechaprestamo_value;

    public FechaPrestamo() {
        this.fechaprestamo_value = LocalDate.now();
    }
}
