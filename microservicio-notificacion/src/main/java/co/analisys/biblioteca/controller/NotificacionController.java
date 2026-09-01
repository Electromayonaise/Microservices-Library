package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.dto.NotificacionDTO;
import co.analisys.biblioteca.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificar")
@Tag(name = "Notificacion", description = "Envío de notificaciones a los usuarios")
public class NotificacionController {
    @Autowired
    private NotificacionService notificacionService;

    @Operation(
            summary = "Enviar una notificación",
            description = "Envía una notificación a un usuario. Es invocado por otros servicios " +
                    "(por ejemplo, circulación) para informar eventos como préstamos o devoluciones."
    )
    @PostMapping
    public void enviarNotificacion(@RequestBody NotificacionDTO notificacion) {
        notificacionService.enviarNotificacion(notificacion);
    }
}