package Foro_hub.api.domain.topico.repositorio;

import Foro_hub.api.domain.topico.Estado;
import Foro_hub.api.domain.topico.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {
    @SuppressWarnings("null")
    Page<Topico> findALl(Pageable paginacion);

    Page<Topico> findAllbyEstadoIsNot(Estado estado, Pageable paginacion);

    Boolean existsByTituloAndMensaje(String titulo, String mensaje);

    Topico findByTitulo(String titulo);
}
