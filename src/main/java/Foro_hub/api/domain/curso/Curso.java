package Foro_hub.api.domain.curso;


import Foro_hub.api.domain.curso.dto.ActualizarCurso;
import Foro_hub.api.domain.curso.dto.CrearCurso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cursos")
@Entity(name = "Curso")
@EqualsAndHashCode(of = "id")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    private Boolean activo;


    public Curso(){

    }

    public Curso (CrearCurso crearCurso) {
        this.nombre = crearCurso.nombre();
        this.categoria = crearCurso.categoria();
        this.activo = true;
    }

    public void actualizarCurso(ActualizarCurso actualizarCurso) {
        if(actualizarCurso.nombre() != null) {
            this.nombre = actualizarCurso.nombre();
        }
        if(actualizarCurso.categoria() != null) {
            this.categoria = actualizarCurso.categoria();
        }
        if(actualizarCurso.activo() != null) {
            this.activo = actualizarCurso.activo();
        }
    }

    public void eliminarCurso() {this.activo = false;}

    //getter
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Boolean getActivo() {
        return activo;
    }

}


