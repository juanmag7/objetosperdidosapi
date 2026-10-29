package co.edu.autonoma.objetosperdidosapi.repository;

import co.edu.autonoma.objetosperdidosapi.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}