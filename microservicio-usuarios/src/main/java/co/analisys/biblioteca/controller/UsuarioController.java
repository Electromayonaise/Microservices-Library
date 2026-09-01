package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.model.Email;
import co.analisys.biblioteca.model.Usuario;
import co.analisys.biblioteca.model.UsuarioId;
import co.analisys.biblioteca.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Gestión de la información de los usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Consultar un usuario",
            description = "Obtiene la información de un usuario a partir de su identificador."
    )
    @GetMapping("/{id}")
    public Usuario obtenerUsuario(@PathVariable String id) {
        return usuarioService.obtenerUsuario(new UsuarioId(id));
    }

    @Operation(
            summary = "Actualizar el email de un usuario",
            description = "Actualiza la dirección de correo electrónico registrada para un usuario."
    )
    @PutMapping("/{id}/email")
    public void cambiarEmail(@PathVariable String id, @RequestBody String nuevoEmail) {
        usuarioService.cambiarEmailUsuario(new UsuarioId(id), new Email(nuevoEmail));
    }
}
