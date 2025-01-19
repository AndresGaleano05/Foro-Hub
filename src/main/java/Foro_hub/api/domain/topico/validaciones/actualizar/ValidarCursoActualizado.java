package Foro_hub.api.domain.topico.validaciones.actualizar;

import Foro_hub.api.domain.curso.repositorio.CursoRepository;
import Foro_hub.api.domain.topico.dto.ActualizarTopico;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarCursoActualizado implements ValidarTopicoActualizado{

    @Autowired
    private CursoRepository repository;

    @Override
    public void validador(ActualizarTopico datos) {
        if(datos.cursoId() != null){
            var ExisteCurso = repository.existsById(datos.cursoId());
            if (!ExisteCurso){
                throw new ValidationException("Este curso no existe");
            }

            var cursoHabilitado = repository.findById(datos.cursoId()).get().getActivo();
            if(!cursoHabilitado){
                throw new ValidationException("Este curso no esta disponible en este momento.");
            }
        }

    }
}
