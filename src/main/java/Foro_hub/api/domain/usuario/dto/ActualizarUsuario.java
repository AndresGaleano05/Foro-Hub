package Foro_hub.api.domain.usuario.dto;

import Foro_hub.api.domain.usuario.Perfiles;

public record ActualizarUsuario(
        String nombre,
        String email,
        String contraseña,
        Perfiles perfiles,
        Boolean borrarUsuario
) {
}
