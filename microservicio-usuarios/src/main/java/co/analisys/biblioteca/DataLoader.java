package co.analisys.biblioteca;

import co.analisys.biblioteca.model.*;
import co.analisys.biblioteca.repository.UsuarioRepository;
import jakarta.persistence.Embedded;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        // Cargar datos de prueba
        UsuarioId usuarioId = new UsuarioId("1");
        Email email = new Email("jrquintero@analisys.co");
        Direccion direccion = new Direccion("Calle 123","Cali","123");
        Credenciales credenciales = new Credenciales("jrquintero","password");
        usuarioRepository.save(Usuario.builder()
                .id(usuarioId)
                .nombre("Jhon Robert Quintero")
                .email(email)
                .direccion(direccion)
                .credenciales(credenciales)
                .build());

        System.out.println("Datos de prueba cargados exitosamente.");
    }
}
