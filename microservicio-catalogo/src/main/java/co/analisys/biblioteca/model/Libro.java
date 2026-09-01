package co.analisys.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Libro {
    @EmbeddedId
    private LibroId id;

    private String titulo;

    @Embedded
    private ISBN isbn;

    @Embedded
    private Categoria categoria;

    private boolean disponible;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Autor> autores;

    public void marcarComoDisponible() {
        this.disponible = true;
    }

    public void marcarComoNoDisponible() {
        this.disponible = false;
    }

    public void actualizarCategoria(Categoria nuevaCategoria) {
        this.categoria = nuevaCategoria;
    }
}
