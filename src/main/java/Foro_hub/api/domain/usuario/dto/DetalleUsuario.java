package Foro_hub.api.domain.usuario.dto;

import Foro_hub.api.domain.usuario.Perfiles;
import Foro_hub.api.domain.usuario.Usuario;

public record DetalleUsuario(
        Long id,
        String nombre,
        Perfiles perfiles,
        String email,
        Boolean borrarUsuario
) {

    public DetalleUsuario(Usuario usuario){
        this(usuario.getId(),
                usuario.getNombre(),
                usuario.getPerfiles(),
                usuario.getEmail(),
                usuario.getHabilitado()
        );
    }
}
