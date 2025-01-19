package Foro_hub.api.domain.usuario.validaciones.actualizar;


import Foro_hub.api.domain.usuario.dto.ActualizarUsuario;
import Foro_hub.api.domain.usuario.repositorio.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidaActualizacionUsuario implements ValidarActualizacionUsuario{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void validador(ActualizarUsuario datos) {
        if (datos.email() != null) {
            var emailDuplicado = usuarioRepository.findByEmail(datos.email());

            if (emailDuplicado != null) {
                throw new ValidationException("Este email se encuentra en uso");
            }
        }
    }

}
