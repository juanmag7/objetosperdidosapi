package co.edu.autonoma.objetosperdidosapi.repository;

import co.edu.autonoma.objetosperdidosapi.entity.ReclamacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReclamacionRepository
        extends JpaRepository<ReclamacionEntity, Long> {
}