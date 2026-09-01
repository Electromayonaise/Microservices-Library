package co.analisys.biblioteca.service;

import co.analisys.biblioteca.model.Libro;
import co.analisys.biblioteca.model.LibroId;
import co.analisys.biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogoService {
    private final LibroRepository libroRepository;

    @Autowired
    public CatalogoService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public Libro obtenerLibro(LibroId id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    @Transactional
    public void actualizarDisponibilidad(LibroId libroId, boolean disponible) {
        Libro libro = obtenerLibro(libroId);
        if (disponible) {
            libro.marcarComoDisponible();
        } else {
            libro.marcarComoNoDisponible();
        }
        libroRepository.save(libro);
    }

    public List<Libro> buscarLibros(String criterio) {
        return libroRepository.findByTitulo(criterio);
    }
}