package co.edu.autonoma.objetosperdidosapi.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    public DetalleVerificacionEntity() {
    }

    @PrePersist
    @PreUpdate
    public void actualizarFecha() {
        fechaActualizacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public HallazgoEntity getHallazgo() {
        return hallazgo;
    }

    public void setHallazgo(HallazgoEntity hallazgo) {
        this.hallazgo = hallazgo;
    }

    public String getDescripcionVerificacionPrivada() {
        return descripcionVerificacionPrivada;
    }

    public void setDescripcionVerificacionPrivada(
            String descripcionVerificacionPrivada) {
        this.descripcionVerificacionPrivada = descripcionVerificacionPrivada;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}