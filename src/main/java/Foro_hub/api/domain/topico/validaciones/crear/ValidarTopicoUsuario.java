package Foro_hub.api.domain.topico.validaciones.crear;

import Foro_hub.api.domain.topico.dto.CrearTopico;
import Foro_hub.api.domain.usuario.repositorio.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarTopicoUsuario implements ValidarTopicoCreado {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validador(CrearTopico datos) {
        var existeUsuario =usuarioRepository.existsById(datos.usuarioId());
        if(!existeUsuario) {
            throw new ValidationException("Este usuario no existe");
        }

        var usuarioHabilitado = usuarioRepository.findById(datos.usuarioId()).get().getHabilitado();
        if (!usuarioHabilitado){
            throw new ValidationException("Este usuario no esta habilitado");
        }
    }
}
