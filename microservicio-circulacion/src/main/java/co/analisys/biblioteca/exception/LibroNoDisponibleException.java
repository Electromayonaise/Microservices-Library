package co.analisys.biblioteca.exception;

import co.analisys.biblioteca.model.LibroId;

public class LibroNoDisponibleException extends RuntimeException {

    public LibroNoDisponibleException(LibroId libroId) {
        super("El libro con ID " + libroId.getLibroid_value() + " no está disponible.");
    }
}
