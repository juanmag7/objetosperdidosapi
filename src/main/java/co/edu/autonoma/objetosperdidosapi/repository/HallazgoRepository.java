package co.edu.autonoma.objetosperdidosapi.repository;

import co.edu.autonoma.objetosperdidosapi.entity.HallazgoEntity;
import co.edu.autonoma.objetosperdidosapi.enums.EstadoHallazgo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HallazgoRepository
        extends JpaRepository<HallazgoEntity, Long> {

    List<HallazgoEntity> findByEstado(EstadoHallazgo estado);

    Optional<HallazgoEntity> findByIdAndEstado(
            Long id,
            EstadoHallazgo estado
    );
}