package co.analisys.biblioteca.service;

import co.analisys.biblioteca.dto.NotificacionDTO;
import co.analisys.biblioteca.exception.LibroNoDisponibleException;
import co.analisys.biblioteca.exception.PrestamoNoEncontradoException;
import co.analisys.biblioteca.model.*;
import co.analisys.biblioteca.repository.PrestamoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CirculacionService {
    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public void prestarLibro(UsuarioId usuarioId, LibroId libroId) {
        Boolean libroDisponible = restTemplate.getForObject(
                "http://localhost:8082/libros/" + libroId.getLibroid_value() + "/disponible", Boolean.class);

        if (libroDisponible != null && libroDisponible) {
            Prestamo prestamo = new Prestamo(
                    new PrestamoId(java.util.UUID.randomUUID().toString()),
                    usuarioId,
                    libroId,
                    new FechaPrestamo(),
                    new FechaDevolucionPrevista(),
                    EstadoPrestamo.ACTIVO
            );
            prestamoRepository.save(prestamo);

            // Actualizar disponibilidad
            HttpEntity<Boolean> requestEntity = new HttpEntity<>(false);
            restTemplate.exchange(
                    "http://localhost:8082" + "/libros/" + libroId.getLibroid_value() + "/disponibilidad",
                    HttpMethod.PUT,
                    requestEntity,
                    Void.class
            );

            restTemplate.postForObject(
                    "http://localhost:8084/notificar",
                    new NotificacionDTO(usuarioId.getUsuarioid_value(), "Libro prestado: " + libroId.getLibroid_value()),
                    Void.class);
        } else {
            throw new LibroNoDisponibleException(libroId);
        }
    }

    @Transactional
    public void devolverLibro(PrestamoId prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new PrestamoNoEncontradoException(prestamoId));

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        prestamoRepository.save(prestamo);

        restTemplate.put("http://localhost:8082/libros/" + prestamo.getLibroId().getLibroid_value() + "/disponibilidad", true);

        restTemplate.postForObject(
                "http://localhost:8084/notificar",
                new NotificacionDTO(prestamo.getUsuarioId().getUsuarioid_value(), "Libro devuelto: " + prestamo.getLibroId().getLibroid_value()),
                Void.class);
    }

    public List<Prestamo> obtenerTodosPrestamos() {
        return prestamoRepository.findAll();
    }
}
