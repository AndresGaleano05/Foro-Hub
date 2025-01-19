package Foro_hub.api.domain.respuesta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearRespuesta(
        @NotBlank
        String mensaje,
        @NotNull
        Long usuarioId,
        @NotNull
        Long topicoId) {
}
