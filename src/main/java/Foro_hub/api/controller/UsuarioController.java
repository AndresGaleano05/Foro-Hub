package Foro_hub.api.controller;

import Foro_hub.api.domain.usuario.Usuario;
import Foro_hub.api.domain.usuario.dto.ActualizarUsuario;
import Foro_hub.api.domain.usuario.dto.CrearUsuario;
import Foro_hub.api.domain.usuario.dto.DetalleUsuario;
import Foro_hub.api.domain.usuario.repositorio.UsuarioRepository;
import Foro_hub.api.domain.usuario.validaciones.actualizar.ValidarActualizacionUsuario;
import Foro_hub.api.domain.usuario.validaciones.crear.ValidarCrearUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuario", description = "Crear topicos y publica respuestas")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepositorio;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    List<ValidarCrearUsuario> crearValidador;

    @Autowired
    List<ValidarActualizacionUsuario> actualizarValidador;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra un nuevo usuario en la BD")
    public ResponseEntity<DetalleUsuario> crearUsuario(@RequestBody @Valid CrearUsuario crearUsuario,
                                                       UriComponentsBuilder uriBuilder){
        crearValidador.forEach(v -> v.validador(crearUsuario));

        String hashedPassword = passwordEncoder.encode(crearUsuario.contraseña());
        Usuario usuario = new Usuario(crearUsuario, hashedPassword);

        usuarioRepositorio.save(usuario);
        var uri = uriBuilder.path("/usuarios/{username}").buildAndExpand(usuario.getNombre()).toUri();
        return ResponseEntity.created(uri).body(new DetalleUsuario(usuario));
    }

    @GetMapping("/all")
    @Operation(summary = "Enumera todos los usuarios independientemente de su estado")
    public ResponseEntity<Page<DetalleUsuario>> leerTodosUsuarios(@PageableDefault(size = 5, sort = {"id"}) Pageable paginacion){
        var pagina = usuarioRepositorio.findAll(paginacion).map(DetalleUsuario::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Lista solo usuarios habilitados")
    public ResponseEntity<Page<DetalleUsuario>> leerUsuariosActivos(@PageableDefault(size = 5, sort = {"id"}) Pageable paginacion){
        var pagina = usuarioRepositorio.findAllByHabilitadoTrue(paginacion).map(DetalleUsuario::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Lee un único usuario por su nombre de usuario")
    public ResponseEntity<DetalleUsuario> leerUnUsuario(@PathVariable String nombre){
        Usuario usuario = (Usuario) usuarioRepositorio.findByNombre(nombre);
        var datosUsuario = new DetalleUsuario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getPerfiles(),
                usuario.getEmail(),
                usuario.getHabilitado());

        return ResponseEntity.ok(datosUsuario);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Lee un único usuario por su ID")
    public ResponseEntity<DetalleUsuario>leerUnUsuario(@PathVariable Long id){
        Usuario usuario = usuarioRepositorio.getReferenceById(id);
        var datosUsuario = new DetalleUsuario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getPerfiles(),
                usuario.getEmail(),
                usuario.getHabilitado());

        return ResponseEntity.ok(datosUsuario);
    }

    @PutMapping("/{username}")
    @Transactional
    @Operation(summary = "Actualiza la contraseña de un usuario, perfil, nombre, correo electrónico o estado habilitado")
    public ResponseEntity<DetalleUsuario> actualizarUsuario(@RequestBody @Valid ActualizarUsuario actualizarUsuario, @PathVariable String nombre){
        actualizarValidador.forEach(v -> v.validador(actualizarUsuario));

        Usuario usuario = (Usuario) usuarioRepositorio.findByNombre(nombre);

        if (actualizarUsuario.contraseña() != null){
            String hashedPassword = passwordEncoder.encode(actualizarUsuario.contraseña());
            usuario.actualizarUsuarioConContraseña(actualizarUsuario, hashedPassword);

        }else {
            usuario.actualizarUsuario(actualizarUsuario);
        }

        var datosUsuario = new DetalleUsuario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getPerfiles(),
                usuario.getEmail(),
                usuario.getHabilitado());

        return ResponseEntity.ok(datosUsuario);
    }

    @DeleteMapping("/{username}")
    @Transactional
    @Operation(summary = "Deshabilita a un usuario")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String nombre){
        Usuario usuario = (Usuario) usuarioRepositorio.findByNombre(nombre);
        usuario.eliminarUsuario();
        return ResponseEntity.noContent().build();
    }
}
