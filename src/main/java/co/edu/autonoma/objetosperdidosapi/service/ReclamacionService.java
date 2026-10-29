package co.edu.autonoma.objetosperdidosapi.service;

import co.edu.autonoma.objetosperdidosapi.dto.CrearReclamacionRequest;
import co.edu.autonoma.objetosperdidosapi.dto.ReclamacionResponse;
import co.edu.autonoma.objetosperdidosapi.dto.RegistrarDecisionRequest;
import co.edu.autonoma.objetosperdidosapi.entity.DecisionReclamacionEntity;
import co.edu.autonoma.objetosperdidosapi.entity.HallazgoEntity;
import co.edu.autonoma.objetosperdidosapi.entity.ReclamacionEntity;
import co.edu.autonoma.objetosperdidosapi.entity.UsuarioEntity;
import co.edu.autonoma.objetosperdidosapi.enums.EstadoHallazgo;
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

    // Crear una reclamación
    @Transactional
    public ReclamacionResponse crear(CrearReclamacionRequest request) {

        if (request == null
                || request.hallazgoId() == null
                || request.reclamanteId() == null
                || request.evidenciaPrivada() == null
                || request.evidenciaPrivada().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe indicar el hallazgo, el reclamante y la evidencia privada."
            );
        }

        HallazgoEntity hallazgo = hallazgoRepository
                .findById(request.hallazgoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el hallazgo indicado."
                ));

        if (hallazgo.getEstado() == EstadoHallazgo.ENTREGADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se pueden reclamar objetos que ya fueron entregados."
            );
        }

        if (hallazgo.getEstado() != EstadoHallazgo.PUBLICADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se pueden reclamar objetos publicados."
            );
        }

        UsuarioEntity reclamante = usuarioRepository
                .findById(request.reclamanteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el usuario reclamante."
                ));

        if (!reclamante.isActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "El usuario está inactivo."
            );
        }

        if (reclamante.getRol() != RolUsuario.COMUNIDAD) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo los usuarios de la comunidad pueden reclamar objetos."
            );
        }

        ReclamacionEntity reclamacion = new ReclamacionEntity(
                hallazgo,
                reclamante,
                request.evidenciaPrivada().trim()
        );

        ReclamacionEntity guardada =
                reclamacionRepository.save(reclamacion);

        return ReclamacionResponse.desde(guardada);
    }

    // Consultar una reclamación por su ID
    @Transactional(readOnly = true)
    public Optional<ReclamacionResponse> buscar(Long id) {

        return reclamacionRepository.findById(id)
                .map(ReclamacionResponse::desde);
    }

    // Aprobar o rechazar una reclamación y guardar la auditoría
    @Transactional
    public ReclamacionResponse registrarDecision(
            Long reclamacionId,
            RegistrarDecisionRequest request) {

        if (request == null
                || request.decision() == null
                || request.decididoPorId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe indicar la decisión y el usuario de seguridad."
            );
        }

        ReclamacionEntity reclamacion = reclamacionRepository
                .findById(reclamacionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la reclamación indicada."
                ));

        UsuarioEntity decisor = usuarioRepository
                .findById(request.decididoPorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el usuario que toma la decisión."
                ));

        if (!decisor.isActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "El usuario de seguridad está inactivo."
            );
        }

        if (decisor.getRol() != RolUsuario.SEGURIDAD) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo el personal de seguridad puede decidir reclamaciones."
            );
        }

        try {
            reclamacion.registrarDecision(request.decision());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    e.getMessage()
            );
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            );
        }

        DecisionReclamacionEntity decision = new DecisionReclamacionEntity(
                reclamacion,
                decisor,
                request.decision(),
                request.observacion()
        );

        reclamacionRepository.save(reclamacion);
        decisionReclamacionRepository.save(decision);

        return ReclamacionResponse.desde(reclamacion);
    }
}