package co.analisys.biblioteca;

import co.analisys.biblioteca.model.*;
import co.analisys.biblioteca.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Crear algunos préstamos de ejemplo
        Prestamo prestamo1 = new Prestamo(
                new PrestamoId(UUID.randomUUID().toString()),
                new UsuarioId("U001"),
                new LibroId("L001"),
                new FechaPrestamo(LocalDate.now().minusDays(5)),
                new FechaDevolucionPrevista(LocalDate.now().plusDays(9)),
                EstadoPrestamo.ACTIVO
        );

        Prestamo prestamo2 = new Prestamo(
                new PrestamoId(UUID.randomUUID().toString()),
                new UsuarioId("U002"),
                new LibroId("L002"),
                new FechaPrestamo(LocalDate.now().minusDays(10)),
                new FechaDevolucionPrevista(LocalDate.now().plusDays(4)),
                EstadoPrestamo.ACTIVO
        );

        Prestamo prestamo3 = new Prestamo(
                new PrestamoId(UUID.randomUUID().toString()),
                new UsuarioId("U003"),
                new LibroId("L003"),
                new FechaPrestamo(LocalDate.now().minusDays(15)),
                new FechaDevolucionPrevista(LocalDate.now().minusDays(1)),
                EstadoPrestamo.VENCIDO
        );

        // Guardar los préstamos en la base de datos
        prestamoRepository.saveAll(Arrays.asList(prestamo1, prestamo2, prestamo3));

        System.out.println("Datos de préstamos de ejemplo cargados exitosamente.");
    }
}