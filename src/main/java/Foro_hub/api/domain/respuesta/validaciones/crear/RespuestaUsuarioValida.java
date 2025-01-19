package Foro_hub.api.domain.respuesta.validaciones.crear;


import Foro_hub.api.domain.respuesta.dto.CrearRespuesta;
import Foro_hub.api.domain.usuario.repositorio.UsuarioRepository;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RespuestaUsuarioValida implements ValidarRespuestaCreada{

    @Autowired
    private UsuarioRepository repositorio;

    @Override
    public void validador (CrearRespuesta datos) {
        var usuarioExiste = repositorio.existsById(datos.usuarioId());

        if(!usuarioExiste){
            throw new ValidationException("El usuario no existe");
        }

        var usuarioHabilitado = repositorio.findById(datos.usuarioId()).get().isEnabled();

        if(!usuarioHabilitado){
            throw new ValidationException("El usuario no esta habilitado");
        }
    }
}
