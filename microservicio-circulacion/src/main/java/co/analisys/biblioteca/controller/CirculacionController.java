package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.model.LibroId;
import co.analisys.biblioteca.model.Prestamo;
import co.analisys.biblioteca.model.PrestamoId;
import co.analisys.biblioteca.model.UsuarioId;
import co.analisys.biblioteca.service.CirculacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/circulacion")
@Tag(name = "Circulacion", description = "Préstamo y devolución de libros")
public class CirculacionController {
    @Autowired
    private CirculacionService circulacionService;

    @Operation(
            summary = "Prestar un libro",
            description = "Registra el préstamo de un libro a un usuario. Verifica la disponibilidad " +
                    "del libro en el servicio de catálogo, marca el libro como no disponible y notifica " +
                    "al usuario del préstamo."
    )
    @ApiResponse(responseCode = "200", description = "Préstamo registrado correctamente")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente, inválido o expirado")
    @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol ROLE_LIBRARIAN")
    @PostMapping("/prestar")
    @PreAuthorize("hasAuthority('ROLE_LIBRARIAN')")
    public void prestarLibro(@RequestParam String usuarioId, @RequestParam String libroId) {
        circulacionService.prestarLibro(new UsuarioId(usuarioId), new LibroId(libroId));
    }

    @Operation(
            summary = "Devolver un libro",
            description = "Registra la devolución de un préstamo existente, marca el libro como " +
                    "disponible nuevamente y notifica al usuario de la devolución."
    )
    @ApiResponse(responseCode = "200", description = "Devolución registrada correctamente")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente, inválido o expirado")
    @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol ROLE_LIBRARIAN")
    @PostMapping("/devolver")
    @PreAuthorize("hasAuthority('ROLE_LIBRARIAN')")
    public void devolverLibro(@RequestParam String prestamoId) {
        circulacionService.devolverLibro(new PrestamoId(prestamoId));
    }

    @Operation(
            summary = "Consultar todos los préstamos",
            description = "Este endpoint permite obtener una lista de todos los préstamos registrados " +
                    "en el sistema. Es importante que el cliente esté registrado previamente en la " +
                    "base de datos, de lo contrario no podrá acceder a esta información."
    )
    @ApiResponse(responseCode = "200", description = "Lista de préstamos obtenida correctamente")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente, inválido o expirado")
    @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ROLE_LIBRARIAN ni ROLE_USER")
    @GetMapping("/prestamos")
    @PreAuthorize("hasAnyAuthority('ROLE_LIBRARIAN', 'ROLE_USER')")
    public List<Prestamo> obtenerTodosPrestamos() {
        return circulacionService.obtenerTodosPrestamos();
    }

    @Operation(
            summary = "Estado del servicio",
            description = "Endpoint público, sin autenticación, para verificar que el servicio de " +
                    "circulación está en funcionamiento."
    )
    @GetMapping("/public/status")
    public String getPublicStatus() {
        return "El servicio de circulación está funcionando correctamente";
    }
}
