package Foro_hub.api.domain.respuesta.repositorio;

import Foro_hub.api.domain.respuesta.Respuesta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

    Page<Respuesta> findAllByTopicoId(Long topicoId, Pageable paginacion);

    Page<Respuesta> findALllByUsuarioId(Long usuarioId, Pageable paginacion);

    Respuesta getReferenceByTopicoId(Long id);

    @SuppressWarnings("null")
    Respuesta getReferenceById(Long id);
}
