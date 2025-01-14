package Foro_hub.api.domain.curso.dto;

import Foro_hub.api.domain.curso.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearCurso(
        @NotBlank
        String nombre,
        @NotNull Categoria categoria) {
}
