package Foro_hub.api.controller;


import Foro_hub.api.domain.curso.Curso;
import Foro_hub.api.domain.curso.repositorio.CursoRepository;
import Foro_hub.api.domain.respuesta.Respuesta;
import Foro_hub.api.domain.respuesta.dto.DetalleRespuesta;
import Foro_hub.api.domain.respuesta.repositorio.RespuestaRepository;
import Foro_hub.api.domain.topico.Estado;
import Foro_hub.api.domain.topico.Topico;
import Foro_hub.api.domain.topico.dto.ActualizarTopico;
import Foro_hub.api.domain.topico.dto.CrearTopico;
import Foro_hub.api.domain.topico.dto.DetalleTopico;
import Foro_hub.api.domain.topico.repositorio.TopicoRepository;
import Foro_hub.api.domain.topico.validaciones.actualizar.ValidarTopicoActualizado;
import Foro_hub.api.domain.topico.validaciones.crear.ValidarTopicoCreado;
import Foro_hub.api.domain.usuario.Usuario;
import Foro_hub.api.domain.usuario.repositorio.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Topico", description = "Se vincula a un curso y usuario especifico")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepositorio;

    @Autowired
    private UsuarioRepository usuarioRepositorio;

    @Autowired
    CursoRepository cursoRepositorio;

    @Autowired
    RespuestaRepository respuestaRepositorio;

    @Autowired
    List<ValidarTopicoCreado> crearValidador;

    @Autowired
    List<ValidarTopicoActualizado> actualizaValidador;

    @PostMapping
    @Transactional
    @Operation(summary = "Regista en la base de datos un nuevo topico")
    public ResponseEntity<DetalleTopico>
    crearTopico(@RequestBody @Valid CrearTopico crearTopico,
                UriComponentsBuilder uriBuilder) {
        crearValidador.forEach(v-> v.validador(crearTopico));

        Usuario usuario = usuarioRepositorio.findById(crearTopico.cursoId()).get();
        Curso curso = cursoRepositorio.findById(crearTopico.cursoId()).get();
        Topico topico= new Topico(crearTopico, usuario, curso);

        topicoRepositorio.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalleTopico(topico));
    }

    @GetMapping("/all")
    @Operation(summary = "Lee todos los temas independientemente de su estado")
    public ResponseEntity<Page<DetalleTopico>> leerTodosTopicos(@PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Sort.Direction.DESC) Pageable paginacion){
        var pagina = topicoRepositorio.findAll(paginacion).map(DetalleTopico::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Lista de temas abiertos y cerrados")
    public ResponseEntity<Page<DetalleTopico>> leerTopicosNoEliminados(@PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Sort.Direction.DESC) Pageable paginacion){
        var pagina = topicoRepositorio.findAllByEstadoIsNot(Estado.BORRADO, paginacion).map(DetalleTopico::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lee un único tema por su ID")
    public ResponseEntity<DetalleTopico> leerUnTopico(@PathVariable Long id){
        Topico topico = topicoRepositorio.getReferenceById(id);
        var datosTopico = new DetalleTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getUltimaActualizacion(),
                topico.getEstado(),
                topico.getUsuario().getNombre(),
                topico.getCurso().getNombre(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(datosTopico);
    }

    @GetMapping("/{id}/solucion")
    @Operation(summary = "Lee la respuesta del topico marcada como su solución")
    public ResponseEntity<DetalleRespuesta> leerSolucionTopico(@PathVariable Long id){
        Respuesta respuesta = respuestaRepositorio.getReferenceByTopicoId(id);

        var datosRespuesta = new DetalleRespuesta(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getUltimaActualizacion(),
                respuesta.getSolucion(),
                respuesta.getBorrado(),
                respuesta.getUsuario().getId(),
                respuesta.getUsuario().getNombre(),
                respuesta.getTopico().getId(),
                respuesta.getTopico().getTitulo()
        );
        return ResponseEntity.ok(datosRespuesta);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Actualiza el título, el mensaje, el estado o el ID del curso de un tema")
    public ResponseEntity<DetalleTopico> actualizarTopico(@RequestBody @Valid ActualizarTopico actualizarTopico, @PathVariable Long id){
        actualizaValidador.forEach(v -> v.validador(actualizarTopico));

        Topico topico = topicoRepositorio.getReferenceById(id);

        if(actualizarTopico.cursoId() != null){
            Curso curso = cursoRepositorio.getReferenceById(actualizarTopico.cursoId());
            topico.actualizarTopicoConCurso(actualizarTopico, curso);
        }else{
            topico.actualizarTopico(actualizarTopico);
        }

        var datosTopico = new DetalleTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getUltimaActualizacion(),
                topico.getEstado(),
                topico.getUsuario().getNombre(),
                topico.getCurso().getNombre(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(datosTopico);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Elimina 1 topico")
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id){
        Topico topico = topicoRepositorio.getReferenceById(id);
        topico.eliminarTopico();
        return ResponseEntity.noContent().build();
    }
}












