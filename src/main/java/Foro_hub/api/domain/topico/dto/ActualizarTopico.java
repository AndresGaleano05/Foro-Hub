package Foro_hub.api.domain.topico.dto;

import Foro_hub.api.domain.topico.Estado;

public record ActualizarTopico(
        String titulo,
        String mensaje,
        Estado estado,
        Long cursoId
) {
}
