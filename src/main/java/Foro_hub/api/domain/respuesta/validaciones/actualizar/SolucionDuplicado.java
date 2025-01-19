package Foro_hub.api.domain.respuesta.validaciones.actualizar;

import Foro_hub.api.domain.respuesta.Respuesta;
import Foro_hub.api.domain.respuesta.dto.ActualizarRespuesta;
import Foro_hub.api.domain.respuesta.repositorio.RespuestaRepository;
import Foro_hub.api.domain.topico.Estado;
import Foro_hub.api.domain.topico.repositorio.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolucionDuplicado implements ValidarRespuestaActualizada {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Override
    public void validador (ActualizarRespuesta datos, Long respuestaId) {
        if (datos.solucion()) {
            Respuesta respuesta = respuestaRepository.getReferenceById(respuestaId);
            var topicoResultado = topicoRepository.getReferenceById(respuesta.getTopico().getId());
            if (topicoResultado.getEstado()== Estado.CERRADO){
                throw new ValidationException("Topico ya se encuentra solucionado");
            }
        }
    }
}
