package Foro_hub.api.domain.topico;


import Foro_hub.api.domain.curso.Curso;
import Foro_hub.api.domain.topico.dto.ActualizarTopico;
import Foro_hub.api.domain.topico.dto.CrearTopico;
import Foro_hub.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Table(name = "topico")
@Entity(name = "Topico")
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String titulo;
    private String mensaje;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    public Topico() {

    }

    public Topico(CrearTopico crearTopico, Usuario usuario, Curso curso) {
        this.titulo = crearTopico.titulo();
        this.mensaje = crearTopico.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.ultimaActualizacion = LocalDateTime.now();
        this.estado = Estado.ABIERTO;
        this.usuario = usuario;
        this.curso = curso;
    }

    public void actualizarTopicoConCurso(ActualizarTopico actualizarTopico, Curso curso){
        if (actualizarTopico.titulo() != null){
            this.titulo = actualizarTopico.titulo();
        }
        if (actualizarTopico.mensaje()!=null){
            this.mensaje= actualizarTopico.mensaje();
        }
        if (actualizarTopico.estado()!=null){
            this.estado= actualizarTopico.estado();
        }
        if (actualizarTopico.cursoId()!=null){
            this.curso= curso;
        }
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void actualizarTopico(ActualizarTopico actualizarTopico){
        if (actualizarTopico.titulo() != null){
            this.titulo = actualizarTopico.titulo();
        }
        if (actualizarTopico.mensaje()!=null){
            this.mensaje= actualizarTopico.mensaje();
        }
        if (actualizarTopico.estado()!=null){
            this.estado= actualizarTopico.estado();
        }
        if (actualizarTopico.cursoId()!=null){
            this.curso= curso;
        }
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void eliminarTopico(){
        this.estado = Estado.BORRADO;
    }

    public void setEstado(Estado estado){
        this.estado = estado;
    }

    //Getter

    public long getId() {
        return Id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public Estado getEstado() {
        return estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Curso getCurso() {
        return curso;
    }
}

