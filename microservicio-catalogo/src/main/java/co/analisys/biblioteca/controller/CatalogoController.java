package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.model.Libro;
import co.analisys.biblioteca.model.LibroId;
import co.analisys.biblioteca.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libros")
@Tag(name = "Catalogo", description = "Consulta y actualización del catálogo de libros")
public class CatalogoController {
    private final CatalogoService catalogoService;

    @Autowired
    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @Operation(
            summary = "Consultar un libro",
            description = "Obtiene la información de un libro del catálogo a partir de su identificador."
    )
    @ApiResponse(responseCode = "200", description = "Libro encontrado")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente, inválido o expirado")
    @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ROLE_LIBRARIAN ni ROLE_USER")
    @PreAuthorize("hasAnyAuthority('ROLE_LIBRARIAN', 'ROLE_USER')")
    @GetMapping("/{id}")
    public Libro obtenerLibro(@PathVariable String id) {
        return catalogoService.obtenerLibro(new LibroId(id));
    }

    @Operation(
            summary = "Consultar disponibilidad de un libro",
            description = "Indica si un libro está disponible para préstamo. Es utilizado por el " +
                    "servicio de circulación antes de registrar un préstamo."
    )
    @ApiResponse(responseCode = "200", description = "Disponibilidad consultada correctamente")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente, inválido o expirado")
    @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ROLE_LIBRARIAN ni ROLE_USER")
    @PreAuthorize("hasAnyAuthority('ROLE_LIBRARIAN', 'ROLE_USER')")
    @GetMapping("/{id}/disponible")
    public boolean isLibroDisponible(@PathVariable String id) {
        Libro libro = catalogoService.obtenerLibro(new LibroId(id));
        return libro != null && libro.isDisponible();
    }

    @Operation(
            summary = "Actualizar disponibilidad de un libro",
            description = "Actualiza el estado de disponibilidad de un libro. Es invocado por el " +
                    "servicio de circulación al prestar o devolver un libro."
    )
    @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada correctamente")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente, inválido o expirado")
    @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol ROLE_LIBRARIAN")
    @PreAuthorize("hasAuthority('ROLE_LIBRARIAN')")
    @PutMapping("/{id}/disponibilidad")
    public void actualizarDisponibilidad(@PathVariable String id, @RequestBody boolean disponible) {
        catalogoService.actualizarDisponibilidad(new LibroId(id), disponible);
    }

    @Operation(
            summary = "Buscar libros",
            description = "Busca libros en el catálogo que coincidan con el criterio de búsqueda " +
                    "proporcionado (título, autor o categoría)."
    )
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada correctamente")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente, inválido o expirado")
    @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ROLE_LIBRARIAN ni ROLE_USER")
    @PreAuthorize("hasAnyAuthority('ROLE_LIBRARIAN', 'ROLE_USER')")
    @GetMapping("/buscar")
    public List<Libro> buscarLibros(@RequestParam String criterio) {
        return catalogoService.buscarLibros(criterio);
    }
}
