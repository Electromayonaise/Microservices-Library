package co.analisys.biblioteca.repository;

import co.analisys.biblioteca.model.Libro;
import co.analisys.biblioteca.model.LibroId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, LibroId> {

    List<Libro> findByTitulo(String titulo);

}
