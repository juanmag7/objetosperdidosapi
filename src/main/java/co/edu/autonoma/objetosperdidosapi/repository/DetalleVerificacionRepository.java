package co.edu.autonoma.objetosperdidosapi.repository;

import co.edu.autonoma.objetosperdidosapi.entity.DetalleVerificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVerificacionRepository
        extends JpaRepository<DetalleVerificacionEntity, Long> {
}