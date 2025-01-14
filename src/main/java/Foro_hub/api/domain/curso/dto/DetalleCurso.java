package Foro_hub.api.domain.curso.dto;

import Foro_hub.api.domain.curso.Categoria;
import Foro_hub.api.domain.curso.Curso;

public record DetalleCurso(
        Long id,
        String nombre,
        Categoria categoria,
        Boolean activo) {
    public DetalleCurso(Curso curso) {
        this(
               curso.getId(),
               curso.getNombre(),
               curso.getCategoria(),
               curso.getActivo());
    }

}
