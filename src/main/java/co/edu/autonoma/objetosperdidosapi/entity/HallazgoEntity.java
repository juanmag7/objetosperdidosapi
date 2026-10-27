package co.edu.autonoma.objetosperdidosapi.entity;

import co.edu.autonoma.objetosperdidosapi.enums.EstadoHallazgo;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "hallazgos")
public class HallazgoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 120)
    private String titulo;

    @Column(name = "descripcion_publica", nullable = false, columnDefinition = "text")
    private String descripcionPublica;

    @Column(name = "categoria", nullable = false, length = 60)
    private String categoria;

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

    public HallazgoEntity() {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcionPublica() {
        return descripcionPublica;
    }

    public void setDescripcionPublica(String descripcionPublica) {
        this.descripcionPublica = descripcionPublica;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLugarHallazgo() {
        return lugarHallazgo;
    }

    public void setLugarHallazgo(String lugarHallazgo) {
        this.lugarHallazgo = lugarHallazgo;
    }

    public LocalDateTime getFechaHallazgo() {
        return fechaHallazgo;
    }

    public void setFechaHallazgo(LocalDateTime fechaHallazgo) {
        this.fechaHallazgo = fechaHallazgo;
    }

    public EstadoHallazgo getEstado() {
        return estado;
    }

    public void setEstado(EstadoHallazgo estado) {
        this.estado = estado;
    }

    public UsuarioEntity getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(UsuarioEntity registradoPor) {
        this.registradoPor = registradoPor;
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