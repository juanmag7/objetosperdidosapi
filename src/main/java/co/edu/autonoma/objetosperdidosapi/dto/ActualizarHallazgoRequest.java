package co.edu.autonoma.objetosperdidosapi.dto;

import java.time.LocalDateTime;

public record ActualizarHallazgoRequest(
        String titulo,
        String categoria,
        String descripcionPublica,
        String lugarHallazgo,
        LocalDateTime fechaHallazgo,
        Long usuarioSeguridadId
) {
}