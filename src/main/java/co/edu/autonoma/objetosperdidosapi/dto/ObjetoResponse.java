package co.edu.autonoma.objetosperdidosapi.dto;

import co.edu.autonoma.objetosperdidosapi.entity.HallazgoEntity;
import co.edu.autonoma.objetosperdidosapi.enums.EstadoHallazgo;

import java.time.LocalDateTime;

public record ObjetoResponse(
        Long id,
        String titulo,
        String categoria,
        String descripcionPublica,
        String lugarHallazgo,
        LocalDateTime fechaHallazgo,
        EstadoHallazgo estado
) {

    public static ObjetoResponse desde(HallazgoEntity hallazgo) {
        return new ObjetoResponse(
                hallazgo.getId(),
                hallazgo.getTitulo(),
                hallazgo.getCategoria(),
                hallazgo.getDescripcionPublica(),
                hallazgo.getLugarHallazgo(),
                hallazgo.getFechaHallazgo(),
                hallazgo.getEstado()
        );
    }
}