package co.edu.autonoma.objetosperdidosapi.service;

import org.springframework.stereotype.Service;
import co.edu.autonoma.objetosperdidosapi.dto.EstadoResponse;

@Service
public class EstadoService {

    public EstadoResponse consultarEstado() {
        return new EstadoResponse(
                "objetosperdidosapi",
                "disponible"
        );
    }
}