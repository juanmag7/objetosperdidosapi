package co.edu.autonoma.objetosperdidosapi.controller;

import co.edu.autonoma.objetosperdidosapi.dto.CrearReclamacionRequest;
import co.edu.autonoma.objetosperdidosapi.dto.ReclamacionResponse;
import co.edu.autonoma.objetosperdidosapi.dto.RegistrarDecisionRequest;
import co.edu.autonoma.objetosperdidosapi.service.ReclamacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reclamaciones")
@RequiredArgsConstructor
public class ReclamacionController {

    private final ReclamacionService reclamacionService;

    @PostMapping
    public ResponseEntity<ReclamacionResponse> crear(
            @RequestBody CrearReclamacionRequest request) {

        ReclamacionResponse respuesta =
                reclamacionService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReclamacionResponse> buscar(
            @PathVariable Long id) {

        return reclamacionService.buscar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/decision")
    public ResponseEntity<ReclamacionResponse> registrarDecision(
            @PathVariable Long id,
            @RequestBody RegistrarDecisionRequest request) {

        ReclamacionResponse respuesta =
                reclamacionService.registrarDecision(id, request);

        return ResponseEntity.ok(respuesta);
    }
}