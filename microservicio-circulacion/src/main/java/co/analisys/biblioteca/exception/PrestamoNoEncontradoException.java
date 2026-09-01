package co.analisys.biblioteca.exception;

import co.analisys.biblioteca.model.PrestamoId;

public class PrestamoNoEncontradoException extends RuntimeException {

    public PrestamoNoEncontradoException(PrestamoId prestamoId) {
        super("No se encontró el préstamo con ID " + prestamoId.getPrestamoid_value());
    }
}
