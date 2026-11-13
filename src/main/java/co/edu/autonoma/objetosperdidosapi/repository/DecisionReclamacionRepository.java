package co.edu.autonoma.objetosperdidosapi.repository;

import co.edu.autonoma.objetosperdidosapi.entity.DecisionReclamacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DecisionReclamacionRepository
        extends JpaRepository<DecisionReclamacionEntity, Long> {
}