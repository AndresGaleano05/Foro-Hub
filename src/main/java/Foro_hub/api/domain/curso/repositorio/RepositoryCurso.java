package Foro_hub.api.domain.curso.repositorio;

import Foro_hub.api.domain.curso.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryCurso extends JpaRepository<Curso, Long> {
    Page<Curso> findByActivoTrue(Pageable paginacion);
}
