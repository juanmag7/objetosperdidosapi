package co.edu.autonoma.objetosperdidosapi.dto;

import co.edu.autonoma.objetosperdidosapi.enums.TipoDecision;

public record RegistrarDecisionRequest(
        TipoDecision decision,
        Long decididoPorId,
        String observacion
) {
}