package co.edu.autonoma.objetosperdidosapi.service;

import co.edu.autonoma.objetosperdidosapi.dto.ObjetoResponse;
import co.edu.autonoma.objetosperdidosapi.entity.HallazgoEntity;
import co.edu.autonoma.objetosperdidosapi.enums.EstadoHallazgo;
import co.edu.autonoma.objetosperdidosapi.repository.HallazgoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HallazgoService {

    private final HallazgoRepository hallazgoRepository;

    public List<ObjetoResponse> listarPublicados() {
        return hallazgoRepository
                .findByEstado(EstadoHallazgo.PUBLICADO)
                .stream()
                .map(ObjetoResponse::desde)
                .toList();
    }

    public Optional<ObjetoResponse> buscarPublicado(Long id) {
        return hallazgoRepository
                .findByIdAndEstado(id, EstadoHallazgo.PUBLICADO)
                .map(ObjetoResponse::desde);
    }
}