package Foro_hub.api.domain.topico.dto;

import Foro_hub.api.domain.curso.Categoria;

import Foro_hub.api.domain.topico.Estado;
import Foro_hub.api.domain.topico.Topico;


import java.time.LocalDateTime;

public record DetalleTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        LocalDateTime ultimaActualizacion,
        Estado estado,
        String usuario,
        String curso,
        Categoria categoriaCurso
) {

    public DetalleTopico(Topico topico){
        this(topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getUltimaActualizacion(),
                topico.getEstado(),
                topico.getUsuario().getNombre(),
                topico.getCurso().getNombre(),
                topico.getCurso().getCategoria()
        );
    }
}
