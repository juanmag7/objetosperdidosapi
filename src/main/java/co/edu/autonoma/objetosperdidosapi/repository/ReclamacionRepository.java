package co.edu.autonoma.objetosperdidosapi.repository;

import co.edu.autonoma.objetosperdidosapi.entity.ReclamacionEntity;
import co.edu.autonoma.objetosperdidosapi.enums.EstadoReclamacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReclamacionRepository
        extends JpaRepository<ReclamacionEntity, Long> {

    List<ReclamacionEntity> findByHallazgo_IdAndEstado(
            Long hallazgoId,
            EstadoReclamacion estado
    );
}