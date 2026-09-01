package co.analisys.biblioteca.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Email {

    private final String email_value;
    // constructor y métodos de validación
}
