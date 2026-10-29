package co.edu.autonoma.objetosperdidosapi.entity;

import co.edu.autonoma.objetosperdidosapi.enums.TipoDecision;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(name = "fecha_decision", nullable = false)
    private LocalDateTime fechaDecision;

    public DecisionReclamacionEntity(
            ReclamacionEntity reclamacion,
            UsuarioEntity decididoPor,
            TipoDecision decision,
            String observacion) {

        this.reclamacion = reclamacion;
        this.decididoPor = decididoPor;
        this.decision = decision;
        this.observacion = observacion;
    }

    @PrePersist
    public void antesDeGuardar() {
        if (fechaDecision == null) {
            fechaDecision = LocalDateTime.now();
        }
    }
}