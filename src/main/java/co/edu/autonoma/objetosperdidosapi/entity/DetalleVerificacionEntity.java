package co.edu.autonoma.objetosperdidosapi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "detalles_verificacion")
public class DetalleVerificacionEntity {

    @Id
    @Column(name = "hallazgo_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "hallazgo_id", nullable = false)
    private HallazgoEntity hallazgo;

    @Column(
            name = "descripcion_verificacion_privada",
            nullable = false,
            columnDefinition = "text"
    )
    private String descripcionVerificacionPrivada;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @PrePersist
    @PreUpdate
    public void actualizarFecha() {
        fechaActualizacion = LocalDateTime.now();
    }
}