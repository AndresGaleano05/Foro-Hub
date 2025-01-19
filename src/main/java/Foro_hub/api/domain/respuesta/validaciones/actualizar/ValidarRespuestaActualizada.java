package Foro_hub.api.domain.respuesta.validaciones.actualizar;

import Foro_hub.api.domain.respuesta.dto.ActualizarRespuesta;
import Foro_hub.api.domain.respuesta.dto.CrearRespuesta;

public interface ValidarRespuestaActualizada {
    void validador(ActualizarRespuesta datos, Long respuestaId);
}
