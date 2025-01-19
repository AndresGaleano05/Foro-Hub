package Foro_hub.api.domain.topico.validaciones.crear;

import Foro_hub.api.domain.curso.repositorio.CursoRepository;
import Foro_hub.api.domain.topico.dto.CrearTopico;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarCursoCreado implements ValidarTopicoCreado {

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    public void validador(CrearTopico datos) {
        var ExisteCurso = cursoRepository.existsById(datos.cursoId());
        if(!ExisteCurso) {
            throw new ValidationException("Este curso no existe");
        }

        var cursoHabilitado = cursoRepository.findById(datos.cursoId()).get().getActivo();
        if (!cursoHabilitado){
            throw new ValidationException("Este curso no esta habilitado");
        }
    }

}
