package Foro_hub.api.controller;


import Foro_hub.api.domain.curso.Curso;
import Foro_hub.api.domain.curso.dto.ActualizarCurso;
import Foro_hub.api.domain.curso.dto.CrearCurso;
import Foro_hub.api.domain.curso.dto.DetalleCurso;
import Foro_hub.api.domain.curso.repositorio.CursoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cursos")
@SecurityRequirement(name = "bearer-key")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra nuevo curso en la base de datos.")
    public ResponseEntity<DetalleCurso> crearCurso(@RequestBody @Valid CrearCurso crearCurso,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        Curso curso = cursoRepository.save(new Curso(crearCurso));

        URI uri = uriComponentsBuilder.path("/cursos/{i}").buildAndExpand(curso.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalleCurso(curso));
    }

    @GetMapping
    @Operation(summary = "Listado de cursos activos")
    public ResponseEntity<Page<DetalleCurso>> ListadoCursosActivos(@PageableDefault(size = 5,
            sort = {"id"})Pageable paginacacion) {
        var pagina = cursoRepository.findByActivoTrue(paginacacion).map(DetalleCurso::new);
        return ResponseEntity.ok(pagina);
    }

//    @GetMapping("/{id}")
//    @Operation(summary = "Lee un solo cursor por Id")
//    public ResponseEntity<DetalleCurso> unCurso(@PathVariable Long id) {
//
//        Curso curso = cursoRepository.getReferenceById(id);
//        var datosDelCurso = new DetalleCurso(
//                curso.getId(),
//                curso.getNombre(),
//                curso.getCategoria(),
//                curso.getActivo()
//        );
//            return ResponseEntity.ok(datosDelCurso);
//    }

    @GetMapping("/{id}")
    @Transactional
    @Operation(summary = "Actualiza el nombre, categoria o estodo del curso")
    public ResponseEntity<DetalleCurso> actualizaCurso(@RequestBody @Valid ActualizarCurso actualizarCurso,
                                                       @PathVariable Long id) {
        Curso curso = cursoRepository.getReferenceById(id);

        curso.actualizarCurso(actualizarCurso);

        var datosDelCurso = new DetalleCurso(
                curso.getId(),
                curso.getNombre(),
                curso.getCategoria(),
                curso.getActivo()
        );
        return ResponseEntity.ok(datosDelCurso);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Eliminar un curso")
    public ResponseEntity<?>eliminarCurso(@PathVariable Long id) {
        Curso curso = cursoRepository.getReferenceById(id);
        curso.eliminarCurso();
        return ResponseEntity.noContent().build();
    }

}
