package co.edu.autonoma.objetosperdidosapi.service;

import co.edu.autonoma.objetosperdidosapi.dto.CrearReclamacionRequest;
import co.edu.autonoma.objetosperdidosapi.dto.ReclamacionResponse;
import co.edu.autonoma.objetosperdidosapi.dto.RegistrarDecisionRequest;
import co.edu.autonoma.objetosperdidosapi.entity.DecisionReclamacionEntity;
import co.edu.autonoma.objetosperdidosapi.entity.HallazgoEntity;
import co.edu.autonoma.objetosperdidosapi.entity.ReclamacionEntity;
import co.edu.autonoma.objetosperdidosapi.entity.UsuarioEntity;
import co.edu.autonoma.objetosperdidosapi.enums.EstadoHallazgo;
import co.edu.autonoma.objetosperdidosapi.enums.EstadoReclamacion;
import co.edu.autonoma.objetosperdidosapi.enums.RolUsuario;
import co.edu.autonoma.objetosperdidosapi.repository.DecisionReclamacionRepository;
import co.edu.autonoma.objetosperdidosapi.repository.HallazgoRepository;
import co.edu.autonoma.objetosperdidosapi.repository.ReclamacionRepository;
import co.edu.autonoma.objetosperdidosapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReclamacionService {

    private final ReclamacionRepository reclamacionRepository;
    private final HallazgoRepository hallazgoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DecisionReclamacionRepository decisionReclamacionRepository;

    @Transactional
    public ReclamacionResponse crear(CrearReclamacionRequest request) {
        if (request == null || request.hallazgoId() == null || request.reclamanteId() == null
                || request.evidenciaPrivada() == null || request.evidenciaPrivada().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Debe indicar hallazgo, reclamante y evidencia privada.");
        }
        if (request.evidenciaPrivada().length() > 10000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La evidencia supera 10000 caracteres.");
        }
        HallazgoEntity hallazgo = hallazgoRepository.findById(request.hallazgoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hallazgo no encontrado."));
        if (hallazgo.getEstado() != EstadoHallazgo.PUBLICADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se pueden reclamar objetos publicados y no entregados.");
        }
        UsuarioEntity reclamante = usuarioRepository.findById(request.reclamanteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reclamante no encontrado."));
        if (!reclamante.isActivo() || reclamante.getRol() != RolUsuario.COMUNIDAD) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Se requiere un usuario activo con rol COMUNIDAD.");
        }
        ReclamacionEntity creada = reclamacionRepository.save(
                new ReclamacionEntity(hallazgo, reclamante, request.evidenciaPrivada().trim()));
        return ReclamacionResponse.desde(creada);
    }

    @Transactional(readOnly = true)
    public Optional<ReclamacionResponse> buscar(Long id, Long usuarioId) {
        if (usuarioId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requiere usuarioId.");
        }
        UsuarioEntity solicitante = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));
        if (!solicitante.isActivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario inactivo.");
        }
        Optional<ReclamacionEntity> encontrada = reclamacionRepository.findById(id);
        if (encontrada.isEmpty()) {
            return Optional.empty();
        }
        ReclamacionEntity reclamacion = encontrada.get();
        boolean seguridad = solicitante.getRol() == RolUsuario.SEGURIDAD;
        boolean propietario = reclamacion.getReclamante().getId().equals(usuarioId);
        if (!seguridad && !propietario) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "El usuario indicado no es reclamante ni pertenece a seguridad.");
        }
        return Optional.of(ReclamacionResponse.desde(reclamacion));
    }

    @Transactional
    public ReclamacionResponse registrarDecision(Long id, RegistrarDecisionRequest request) {
        if (request == null || request.decision() == null || request.decididoPorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requieren decision y decididoPorId.");
        }
        if (request.observacion() != null && request.observacion().length() > 5000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La observación supera 5000 caracteres.");
        }
        ReclamacionEntity reclamacion = reclamacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reclamación no encontrada."));
        UsuarioEntity decisor = usuarioRepository.findById(request.decididoPorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));
        if (!decisor.isActivo() || decisor.getRol() != RolUsuario.SEGURIDAD) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Se requiere un usuario activo con rol SEGURIDAD.");
        }
        if (reclamacion.getHallazgo().getEstado() != EstadoHallazgo.PUBLICADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede decidir una reclamación de un objeto no publicado o entregado.");
        }
        if (reclamacion.getEstado() != EstadoReclamacion.PENDIENTE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Solo se pueden decidir reclamaciones pendientes.");
        }
        try {
            reclamacion.registrarDecision(request.decision());
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }
        decisionReclamacionRepository.save(new DecisionReclamacionEntity(
                reclamacion, decisor, request.decision(), request.observacion()));
        reclamacionRepository.save(reclamacion);
        return ReclamacionResponse.desde(reclamacion);
    }

    // DELETE lógico: conserva auditoría y cambia PENDIENTE -> RETIRADA.
    @Transactional
    public void retirar(Long id, Long reclamanteId) {
        if (reclamanteId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requiere reclamanteId.");
        }
        ReclamacionEntity reclamacion = reclamacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reclamación no encontrada."));
        UsuarioEntity solicitante = usuarioRepository.findById(reclamanteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));
        if (!solicitante.isActivo() || solicitante.getRol() != RolUsuario.COMUNIDAD
                || !reclamacion.getReclamante().getId().equals(reclamanteId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Solo el reclamante activo puede retirar su reclamación.");
        }
        try {
            reclamacion.retirar();
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }
        reclamacionRepository.save(reclamacion);
    }
}
