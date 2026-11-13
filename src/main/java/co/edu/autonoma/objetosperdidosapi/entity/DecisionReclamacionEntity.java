package co.edu.autonoma.objetosperdidosapi.entity;

import co.edu.autonoma.objetosperdidosapi.enums.TipoDecision;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "decisiones_reclamacion")
public class DecisionReclamacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reclamacion_id", nullable = false, unique = true)
    private ReclamacionEntity reclamacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "decidido_por_id", nullable = false)
    private UsuarioEntity decididoPor;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision", nullable = false, length = 20)
    private TipoDecision decision;

    @Column(name = "observacion", columnDefinition = "text")
    private String observacion;

    @Column(name = "fecha_decision", nullable = false, updatable = false)
    private LocalDateTime fechaDecision;

    public DecisionReclamacionEntity() {
    }

    @PrePersist
    public void antesDeGuardar() {
        if (fechaDecision == null) {
            fechaDecision = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReclamacionEntity getReclamacion() {
        return reclamacion;
    }

    public void setReclamacion(ReclamacionEntity reclamacion) {
        this.reclamacion = reclamacion;
    }

    public UsuarioEntity getDecididoPor() {
        return decididoPor;
    }

    public void setDecididoPor(UsuarioEntity decididoPor) {
        this.decididoPor = decididoPor;
    }

    public TipoDecision getDecision() {
        return decision;
    }

    public void setDecision(TipoDecision decision) {
        this.decision = decision;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getFechaDecision() {
        return fechaDecision;
    }

    public void setFechaDecision(LocalDateTime fechaDecision) {
        this.fechaDecision = fechaDecision;
    }
}