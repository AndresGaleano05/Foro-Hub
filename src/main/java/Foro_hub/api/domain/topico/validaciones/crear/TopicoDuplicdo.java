package Foro_hub.api.domain.topico.validaciones.crear;


import Foro_hub.api.domain.topico.dto.CrearTopico;
import Foro_hub.api.domain.topico.repositorio.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicoDuplicdo implements ValidarTopicoCreado{

    @Autowired
    private TopicoRepository topicoRepository;

    @Override
    public void validador(CrearTopico datos) {
        var topicoDupliacdo = topicoRepository.existsByTituloAndMensaje(datos.titulo(),datos.mensaje());
        if(topicoDupliacdo){
            throw new ValidationException("Este topico ya existe" + topicoRepository.findByTitulo(datos.titulo()).getId());
        }
    }
}
