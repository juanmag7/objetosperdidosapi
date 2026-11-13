package co.edu.autonoma.objetosperdidosapi.controller;

import co.edu.autonoma.objetosperdidosapi.dto.ObjetoResponse;
import co.edu.autonoma.objetosperdidosapi.service.HallazgoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/objetos")
@RequiredArgsConstructor
public class HallazgoController {

    private final HallazgoService hallazgoService;

    @GetMapping
    public List<ObjetoResponse> listarObjetos() {
        return hallazgoService.listarPublicados();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjetoResponse> buscarObjeto(
            @PathVariable Long id) {

        return hallazgoService.buscarPublicado(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}