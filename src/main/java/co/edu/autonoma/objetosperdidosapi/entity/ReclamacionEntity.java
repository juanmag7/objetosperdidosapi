package co.edu.autonoma.objetosperdidosapi.entity;

import co.edu.autonoma.objetosperdidosapi.enums.EstadoReclamacion;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    @Column(
            name = "evidencia_privada",
            nullable = false,
            columnDefinition = "text"
    )
    private String evidenciaPrivada;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoReclamacion estado = EstadoReclamacion.PENDIENTE;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public ReclamacionEntity() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HallazgoEntity getHallazgo() {
        return hallazgo;
    }

    public void setHallazgo(HallazgoEntity hallazgo) {
        this.hallazgo = hallazgo;
    }

    public UsuarioEntity getReclamante() {
        return reclamante;
    }

    public void setReclamante(UsuarioEntity reclamante) {
        this.reclamante = reclamante;
    }

    public String getEvidenciaPrivada() {
        return evidenciaPrivada;
    }

    public void setEvidenciaPrivada(String evidenciaPrivada) {
        this.evidenciaPrivada = evidenciaPrivada;
    }

    public EstadoReclamacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoReclamacion estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}