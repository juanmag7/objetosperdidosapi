package co.edu.autonoma.objetosperdidosapi.dto;

public record EntregarHallazgoRequest(
        Long reclamacionAprobadaId,
        Long usuarioSeguridadId
) {
}