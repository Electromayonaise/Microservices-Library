package co.analisys.biblioteca.service;

import co.analisys.biblioteca.model.Email;
import co.analisys.biblioteca.model.Usuario;
import co.analisys.biblioteca.model.UsuarioId;
import co.analisys.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario obtenerUsuario(UsuarioId id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void cambiarEmailUsuario(UsuarioId id, Email nuevoEmail) {
        Usuario usuario = obtenerUsuario(id);
        usuario.cambiarEmail(nuevoEmail);
        usuarioRepository.save(usuario);
    }
}