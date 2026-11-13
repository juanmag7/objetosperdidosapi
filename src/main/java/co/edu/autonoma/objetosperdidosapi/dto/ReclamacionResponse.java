package co.edu.autonoma.objetosperdidosapi.dto;

import co.edu.autonoma.objetosperdidosapi.entity.ReclamacionEntity;
import co.edu.autonoma.objetosperdidosapi.enums.EstadoReclamacion;

import java.time.LocalDateTime;

public record ReclamacionResponse(
        Long id,
        Long hallazgoId,
        Long reclamanteId,
        EstadoReclamacion estado,
        LocalDateTime fechaCreacion
) {

    public static ReclamacionResponse desde(
            ReclamacionEntity reclamacion) {

        return new ReclamacionResponse(
                reclamacion.getId(),
                reclamacion.getHallazgo().getId(),
                reclamacion.getReclamante().getId(),
                reclamacion.getEstado(),
                reclamacion.getFechaCreacion()
        );
    }
}