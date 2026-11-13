package co.edu.autonoma.objetosperdidosapi.service;

import co.edu.autonoma.objetosperdidosapi.dto.ActualizarHallazgoRequest;
import co.edu.autonoma.objetosperdidosapi.dto.EntregarHallazgoRequest;
import co.edu.autonoma.objetosperdidosapi.dto.ObjetoResponse;
import co.edu.autonoma.objetosperdidosapi.entity.HallazgoEntity;
import co.edu.autonoma.objetosperdidosapi.entity.ReclamacionEntity;
import co.edu.autonoma.objetosperdidosapi.entity.UsuarioEntity;
import co.edu.autonoma.objetosperdidosapi.enums.EstadoHallazgo;
import co.edu.autonoma.objetosperdidosapi.enums.EstadoReclamacion;
import co.edu.autonoma.objetosperdidosapi.enums.RolUsuario;
import co.edu.autonoma.objetosperdidosapi.repository.HallazgoRepository;
import co.edu.autonoma.objetosperdidosapi.repository.ReclamacionRepository;
import co.edu.autonoma.objetosperdidosapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HallazgoService {

    private final HallazgoRepository hallazgoRepository;
    private final ReclamacionRepository reclamacionRepository;
    private final UsuarioRepository usuarioRepository;

    // Consultar todos los hallazgos publicados.
    @Transactional(readOnly = true)
    public List<ObjetoResponse> listarPublicados() {

        return hallazgoRepository
                .findByEstado(EstadoHallazgo.PUBLICADO)
                .stream()
                .map(ObjetoResponse::desde)
                .toList();
    }

    // Consultar un hallazgo publicado por su identificador.
    @Transactional(readOnly = true)
    public java.util.Optional<ObjetoResponse> buscarPublicado(Long id) {

        return hallazgoRepository
                .findByIdAndEstado(id, EstadoHallazgo.PUBLICADO)
                .map(ObjetoResponse::desde);
    }

    // Actualizar los datos administrativos de un hallazgo.
    @Transactional
    public ObjetoResponse actualizar(
            Long id,
            ActualizarHallazgoRequest request) {

        if (request == null
                || request.titulo() == null
                || request.titulo().isBlank()
                || request.categoria() == null
                || request.categoria().isBlank()
                || request.descripcionPublica() == null
                || request.descripcionPublica().isBlank()
                || request.lugarHallazgo() == null
                || request.lugarHallazgo().isBlank()
                || request.fechaHallazgo() == null
                || request.usuarioSeguridadId() == null
                || request.titulo().length() > 120
                || request.categoria().length() > 60
                || request.lugarHallazgo().length() > 160
                || request.descripcionPublica().length() > 10000) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe proporcionar todos los datos del hallazgo y el usuario de seguridad."
            );
        }

        HallazgoEntity hallazgo = hallazgoRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el hallazgo indicado."
                ));

        validarUsuarioSeguridad(request.usuarioSeguridadId());

        try {
            hallazgo.actualizarDatos(
                    request.titulo().trim(),
                    request.categoria().trim(),
                    request.descripcionPublica().trim(),
                    request.lugarHallazgo().trim(),
                    request.fechaHallazgo()
            );
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    e.getMessage()
            );
        }

        HallazgoEntity actualizado = hallazgoRepository.save(hallazgo);

        return ObjetoResponse.desde(actualizado);
    }

    // Registrar la entrega y cerrar las reclamaciones pendientes.
    @Transactional
    public ObjetoResponse entregar(
            Long hallazgoId,
            EntregarHallazgoRequest request) {

        if (request == null
                || request.reclamacionAprobadaId() == null
                || request.usuarioSeguridadId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe indicar la reclamación aprobada y el usuario de seguridad."
            );
        }

        HallazgoEntity hallazgo = hallazgoRepository
                .findById(hallazgoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el hallazgo indicado."
                ));

        validarUsuarioSeguridad(request.usuarioSeguridadId());

        ReclamacionEntity reclamacionAprobada =
                reclamacionRepository
                        .findById(request.reclamacionAprobadaId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "No existe la reclamación indicada."
                        ));

        if (!reclamacionAprobada.getHallazgo().getId().equals(hallazgoId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La reclamación no corresponde al hallazgo indicado."
            );
        }

        if (reclamacionAprobada.getEstado()
                != EstadoReclamacion.APROBADA) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La entrega requiere una reclamación aprobada."
            );
        }

        try {
            hallazgo.marcarComoEntregado();
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    e.getMessage()
            );
        }

        List<ReclamacionEntity> pendientes =
                reclamacionRepository.findByHallazgo_IdAndEstado(
                        hallazgoId,
                        EstadoReclamacion.PENDIENTE
                );

        pendientes.forEach(ReclamacionEntity::cerrarPorEntrega);

        hallazgoRepository.save(hallazgo);
        reclamacionRepository.saveAll(pendientes);

        return ObjetoResponse.desde(hallazgo);
    }

    // Verificar que el usuario exista, esté activo y tenga rol SEGURIDAD.
    private void validarUsuarioSeguridad(Long usuarioId) {

        UsuarioEntity usuario = usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el usuario de seguridad indicado."
                ));

        if (!usuario.isActivo()
                || usuario.getRol() != RolUsuario.SEGURIDAD) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo un usuario activo de seguridad puede realizar esta operación."
            );
        }
    }
}