package co.edu.autonoma.objetosperdidosapi.entity;

import co.edu.autonoma.objetosperdidosapi.enums.EstadoReclamacion;
import co.edu.autonoma.objetosperdidosapi.enums.TipoDecision;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reclamaciones")
public class ReclamacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hallazgo_id", nullable = false)
    private HallazgoEntity hallazgo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reclamante_id", nullable = false)
    private UsuarioEntity reclamante;

    @Column(name = "evidencia_privada", nullable = false, columnDefinition = "text")
    private String evidenciaPrivada;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoReclamacion estado = EstadoReclamacion.PENDIENTE;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public ReclamacionEntity(
            HallazgoEntity hallazgo,
            UsuarioEntity reclamante,
            String evidenciaPrivada) {

        this.hallazgo = hallazgo;
        this.reclamante = reclamante;
        this.evidenciaPrivada = evidenciaPrivada;
        this.estado = EstadoReclamacion.PENDIENTE;
    }

    public void registrarDecision(TipoDecision decision) {
        if (this.estado != EstadoReclamacion.PENDIENTE) {
            throw new IllegalStateException(
                    "Solo se pueden decidir reclamaciones pendientes."
            );
        }

        if (decision == null) {
            throw new IllegalArgumentException(
                    "La decisión no puede estar vacía."
            );
        }

        if (decision == TipoDecision.APROBADA) {
            this.estado = EstadoReclamacion.APROBADA;
        } else if (decision == TipoDecision.RECHAZADA) {
            this.estado = EstadoReclamacion.RECHAZADA;
        }
    }

    @PrePersist
    public void antesDeGuardar() {
        LocalDateTime ahora = LocalDateTime.now();

        if (fechaCreacion == null) {
            fechaCreacion = ahora;
        }

        if (fechaActualizacion == null) {
            fechaActualizacion = ahora;
        }

        if (estado == null) {
            estado = EstadoReclamacion.PENDIENTE;
        }
    }

    @PreUpdate
    public void antesDeActualizar() {
        fechaActualizacion = LocalDateTime.now();
    }
}