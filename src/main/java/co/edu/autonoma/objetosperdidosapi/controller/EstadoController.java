package co.edu.autonoma.objetosperdidosapi.controller;

import co.edu.autonoma.objetosperdidosapi.dto.EstadoResponse;
import co.edu.autonoma.objetosperdidosapi.service.EstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estado")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoService estadoService;

    @GetMapping
    public EstadoResponse consultarEstado() {
        return estadoService.consultarEstado();
    }
}