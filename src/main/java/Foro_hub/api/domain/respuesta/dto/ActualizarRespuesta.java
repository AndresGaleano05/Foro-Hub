package Foro_hub.api.domain.respuesta.dto;

public record ActualizarRespuesta (
        String mensaje,
        Boolean solucion,
        Boolean borrado
) {
}
