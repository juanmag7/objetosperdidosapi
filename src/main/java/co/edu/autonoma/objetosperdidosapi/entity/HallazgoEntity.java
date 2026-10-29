package co.edu.autonoma.objetosperdidosapi.entity;

import co.edu.autonoma.objetosperdidosapi.enums.EstadoHallazgo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "hallazgos")
public class HallazgoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 120)
    private String titulo;

    @Column(name = "categoria", nullable = false, length = 60)
    private String categoria;

    @Column(name = "descripcion_publica", nullable = false, columnDefinition = "text")
    private String descripcionPublica;

    @Column(name = "lugar_hallazgo", nullable = false, length = 160)
    private String lugarHallazgo;

    @Column(name = "fecha_hallazgo", nullable = false)
    private LocalDateTime fechaHallazgo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoHallazgo estado = EstadoHallazgo.REGISTRADO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registrado_por_id", nullable = false)
    private UsuarioEntity registradoPor;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    public HallazgoEntity(
            String titulo,
            String categoria,
            String descripcionPublica,
            String lugarHallazgo,
            LocalDateTime fechaHallazgo,
            UsuarioEntity registradoPor) {

        this.titulo = titulo;
        this.categoria = categoria;
        this.descripcionPublica = descripcionPublica;
        this.lugarHallazgo = lugarHallazgo;
        this.fechaHallazgo = fechaHallazgo;
        this.registradoPor = registradoPor;
        this.estado = EstadoHallazgo.REGISTRADO;
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
            estado = EstadoHallazgo.REGISTRADO;
        }
    }

    @PreUpdate
    public void antesDeActualizar() {
        fechaActualizacion = LocalDateTime.now();
    }
}