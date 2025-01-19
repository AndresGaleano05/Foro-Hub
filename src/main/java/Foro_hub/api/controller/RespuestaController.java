package Foro_hub.api.controller;

import Foro_hub.api.domain.respuesta.Respuesta;
import Foro_hub.api.domain.respuesta.dto.ActualizarRespuesta;
import Foro_hub.api.domain.respuesta.dto.CrearRespuesta;
import Foro_hub.api.domain.respuesta.dto.DetalleRespuesta;
import Foro_hub.api.domain.respuesta.repositorio.RespuestaRepository;
import Foro_hub.api.domain.respuesta.validaciones.actualizar.ValidarRespuestaActualizada;
import Foro_hub.api.domain.respuesta.validaciones.crear.ValidarRespuestaCreada;
import Foro_hub.api.domain.topico.Estado;
import Foro_hub.api.domain.topico.Topico;
import Foro_hub.api.domain.topico.repositorio.TopicoRepository;
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
@RequestMapping("/respuesta")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Respuesta", description = "solo 1 puede ser la solucion del tema")
public class RespuestaController {
    @Autowired
    private TopicoRepository topicoRepositorio;

    @Autowired
    private UsuarioRepository usuarioRepositorio;

    @Autowired
    private RespuestaRepository respuestaRepositorio;

    @Autowired
    List<ValidarRespuestaCreada> crearValidadores;

    @Autowired
    List<ValidarRespuestaActualizada> actualizarValidadores;

    @PostMapping
    @Transactional
    @Operation(summary = "añade una nueva respuesta a la base de datos, vincula a un usuario y tema existente.")
    public ResponseEntity<DetalleRespuesta> crearRespuesta(@RequestBody @Valid CrearRespuesta crearRespuesta,
                                                           UriComponentsBuilder uriBuilder){
        crearValidadores.forEach(v->v.validador(crearRespuesta));

        Usuario usuario = usuarioRepositorio.getReferenceById(crearRespuesta.usuarioId());
        Topico topico = topicoRepositorio.findById(crearRespuesta.topicoId()).get();

        var respuesta = new Respuesta(crearRespuesta, usuario, topico);
        respuestaRepositorio.save(respuesta);

        var uri = uriBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalleRespuesta(respuesta));
    }

    @GetMapping("/topico/{topicoId}")
    @Operation(summary = "Lee todas las respuestas del tema dado")
    public ResponseEntity<Page<DetalleRespuesta>>
    leerRespuestaDeTopico(@PageableDefault(size = 5, sort = {"ultimaActualizacion"},
        direction = Sort.Direction.ASC)Pageable paginacion, @PathVariable Long topicoId){
        var pagina = respuestaRepositorio.findAllByTopicoId(topicoId, paginacion).map(DetalleRespuesta::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/usuario/{nombreUsuario}")
    @Operation(summary = "Lee las respuestas del nombre del usuario indicado")
    public ResponseEntity<Page<DetalleRespuesta>>
    leerRespuestaDeUsuarios(@PageableDefault(size = 5, sort = {"ultimaActualizacion"},
    direction = Sort.Direction.ASC)Pageable paginacion, @PathVariable Long usuarioId){
        var pagina = respuestaRepositorio.findALllByUsuarioId(usuarioId, paginacion). map(DetalleRespuesta::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Por medio del Id lee solo 1 respuesta")
    public ResponseEntity<DetalleRespuesta> leeUnaRespuesta(@PathVariable Long id) {
        Respuesta respuesta = respuestaRepositorio.getReferenceById(id);

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
                respuesta.getTopico().getTitulo());
        return ResponseEntity.ok(datosRespuesta);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Actualiza el mensaje, la solucion o el estado de la respuesta.")
    public ResponseEntity<DetalleRespuesta> actualizaRespuesta
            (@RequestBody @Valid ActualizarRespuesta actualizarRespuesta,
             @PathVariable Long id){
        actualizarValidadores.forEach(v->v.validador(actualizarRespuesta, id));
        Respuesta respuesta = respuestaRepositorio.getReferenceById(id);
        respuesta.actualizarRespuesta(actualizarRespuesta);

        if (actualizarRespuesta.solucion()) {
            var temaResuelto = topicoRepositorio.getReferenceById(respuesta.getTopico().getId());
            temaResuelto.setEstado(Estado.CERRADO);
        }

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
                respuesta.getTopico().getTitulo());
        return ResponseEntity.ok(datosRespuesta);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Por medio del Id se elimina 1 respuesta")
    public ResponseEntity<?> borrarRespuesta(@PathVariable Long id) {
        Respuesta respuesta = respuestaRepositorio.getReferenceById(id);
        respuesta.eliminarRespuesta();
        return ResponseEntity.noContent().build();
    }
}






