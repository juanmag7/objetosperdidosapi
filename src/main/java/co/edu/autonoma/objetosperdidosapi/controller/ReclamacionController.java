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
    public ResponseEntity<ReclamacionResponse> crear(@RequestBody CrearReclamacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reclamacionService.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReclamacionResponse> buscar(
            @PathVariable Long id, @RequestParam Long usuarioId) {
        return reclamacionService.buscar(id, usuarioId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/decision")
    public ResponseEntity<ReclamacionResponse> registrarDecision(
            @PathVariable Long id, @RequestBody RegistrarDecisionRequest request) {
        return ResponseEntity.ok(reclamacionService.registrarDecision(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> retirar(
            @PathVariable Long id, @RequestParam Long reclamanteId) {
        reclamacionService.retirar(id, reclamanteId);
        return ResponseEntity.noContent().build();
    }
}
