package Foro_hub.api.domain.usuario.repositorio;


import Foro_hub.api.domain.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByNombre(String nombre);

    UserDetails findByEmail(String email);

    Page<Usuario> findAllByHabilitadoTrue(Pageable paginacion);

    @SuppressWarnings("null")
    Usuario getReferenceById(Long id);

    @SuppressWarnings("null")
    Page<Usuario> findAll(Pageable paginacion);

    Usuario getReferenceByNombre(String nombre);

    Boolean existsByNombre(String nombre);

}
