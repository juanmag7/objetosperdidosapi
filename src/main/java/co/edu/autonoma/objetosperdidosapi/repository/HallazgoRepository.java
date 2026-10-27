package co.edu.autonoma.objetosperdidosapi.repository;

import co.edu.autonoma.objetosperdidosapi.entity.HallazgoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallazgoRepository extends JpaRepository<HallazgoEntity, Long> {
}