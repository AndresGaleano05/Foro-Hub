package Foro_hub.api.domain.respuesta;


import Foro_hub.api.domain.respuesta.dto.ActualizarRespuesta;
import Foro_hub.api.domain.respuesta.dto.CrearRespuesta;
import Foro_hub.api.domain.topico.Topico;
import Foro_hub.api.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "respuestas")
@Entity(name = "Respuesta")
@EqualsAndHashCode(of = "id")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private Boolean solucion;
    private Boolean borrado;


    public  Respuesta(CrearRespuesta crearRespuesta, Usuario usuario, Topico topico) {
        this.mensaje = crearRespuesta.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.ultimaActualizacion =LocalDateTime.now();
        this.usuario = usuario;
        this.solucion = false;
        this.topico = topico;
        this.borrado = false;

    }

    public void actualizarRespuesta(ActualizarRespuesta actualizarRespuesta){
        if (actualizarRespuesta.mensaje() != null){
            this.mensaje = actualizarRespuesta.mensaje();
        }
        if (actualizarRespuesta.solucion() !=null){
            this.solucion = actualizarRespuesta.solucion();
        }
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void eliminarRespuesta(){
        this.borrado = true;
    }

    // Getter

    public Long getId() {
        return id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Topico getTopico() {
        return topico;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Boolean getSolucion() {
        return solucion;
    }

    public Boolean getBorrado() {
        return borrado;
    }
}





