package Foro_hub.api.domain.respuesta.validaciones.crear;

import Foro_hub.api.domain.respuesta.dto.CrearRespuesta;
import Foro_hub.api.domain.topico.Estado;
import Foro_hub.api.domain.topico.repositorio.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RespuestaTopicoValida implements ValidarRespuestaCreada {

    @Autowired
    private TopicoRepository repository;

    @Override
    public void validador(CrearRespuesta data) {
        var topicoExiste = repository.existsById(data.topicoId());

        if (!topicoExiste){
            throw new ValidationException("Este topico no existe.");
        }

        var topicoAbierto = repository.findById(data.topicoId()).get().getEstado();

        if(topicoAbierto != Estado.ABIERTO){
            throw new ValidationException("Este topico no esta abierto.");
        }

    }
}

