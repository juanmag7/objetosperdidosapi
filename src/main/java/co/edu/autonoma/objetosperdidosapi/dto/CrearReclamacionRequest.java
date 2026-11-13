package co.edu.autonoma.objetosperdidosapi.dto;

public record CrearReclamacionRequest(
        Long hallazgoId,
        Long reclamanteId,
        String evidenciaPrivada
) {
}