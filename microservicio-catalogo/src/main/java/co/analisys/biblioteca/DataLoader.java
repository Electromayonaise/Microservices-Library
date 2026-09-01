package co.analisys.biblioteca;

import co.analisys.biblioteca.model.*;
import co.analisys.biblioteca.repository.AutorRepository;
import co.analisys.biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create authors
        Autor autor1 = new Autor(null, "Gabriel García Márquez");
        Autor autor2 = new Autor(null, "George Orwell");

        // Save authors to the database
        autorRepository.saveAll(Arrays.asList(autor1, autor2));

        // Create books and associate them with the saved authors
        Libro libro1 = new Libro();
        libro1.setId(new LibroId("1"));
        libro1.setTitulo("Cien años de soledad");
        libro1.setIsbn(new ISBN("978-0307474728"));
        libro1.setCategoria(new Categoria("Ficción"));
        libro1.setDisponible(true);
        libro1.setAutores(Arrays.asList(autor1));

        Libro libro2 = new Libro();
        libro2.setId(new LibroId("2"));
        libro2.setTitulo("1984");
        libro2.setIsbn(new ISBN("978-0451524935"));
        libro2.setCategoria(new Categoria("Ciencia ficción"));
        libro2.setDisponible(true);
        libro2.setAutores(Arrays.asList(autor2));

        // Save books to the database
        libroRepository.saveAll(Arrays.asList(libro1, libro2));

        System.out.println("Datos de prueba cargados exitosamente.");
    }
}
