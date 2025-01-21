package Foro_hub.api.domain.usuario.validaciones.crear;

import Foro_hub.api.domain.usuario.dto.CrearUsuario;
import Foro_hub.api.domain.usuario.repositorio.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDuplicado implements ValidarCrearUsuario{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validador(CrearUsuario datos){
        var usuarioDuplicado = usuarioRepository.findByNombre(datos.nombre());
        if (usuarioDuplicado != null){
            throw new ValidationException("Este usuario ya existe");
        }

        var emailDuplicado = usuarioRepository.findByEmail(datos.email());
        if (emailDuplicado != null) {
            throw new ValidationException("Este email ya existe");
        }
    }
}
