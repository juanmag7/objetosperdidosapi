package co.edu.autonoma.objetosperdidosapi.controller;

import co.edu.autonoma.objetosperdidosapi.dto.ActualizarHallazgoRequest;
import co.edu.autonoma.objetosperdidosapi.dto.EntregarHallazgoRequest;
import co.edu.autonoma.objetosperdidosapi.dto.ObjetoResponse;
import co.edu.autonoma.objetosperdidosapi.service.HallazgoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hallazgos")
@RequiredArgsConstructor
public class HallazgoAdministracionController {

    private final HallazgoService hallazgoService;

    // Actualizar los datos administrativos de un hallazgo.
    @PutMapping("/{id}")
    public ResponseEntity<ObjetoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody ActualizarHallazgoRequest request) {

        ObjetoResponse respuesta =
                hallazgoService.actualizar(id, request);

        return ResponseEntity.ok(respuesta);
    }

    // Registrar la entrega de un hallazgo.
    @PatchMapping("/{id}/entregar")
    public ResponseEntity<ObjetoResponse> entregar(
            @PathVariable Long id,
            @RequestBody EntregarHallazgoRequest request) {

        ObjetoResponse respuesta =
                hallazgoService.entregar(id, request);

        return ResponseEntity.ok(respuesta);
    }
}