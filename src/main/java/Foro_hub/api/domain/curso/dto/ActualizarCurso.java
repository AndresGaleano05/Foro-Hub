package Foro_hub.api.domain.curso.dto;

import Foro_hub.api.domain.curso.Categoria;

public record ActualizarCurso(

        String nombre,
        Categoria categoria,
        Boolean activo) {
}
