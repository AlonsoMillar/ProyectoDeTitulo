package com.example.eventomicroservice.controller;

import com.example.eventomicroservice.dto.EventoRequestDTO;
import com.example.eventomicroservice.dto.EventoResponseDTO;
import com.example.eventomicroservice.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("EventoMicroservice Corriendo");
    }

    // ==========================================
    // ENDPOINTS ADMIN
    // ==========================================

    @PostMapping
    public ResponseEntity<EventoResponseDTO> crearEvento(@Valid @RequestBody EventoRequestDTO dto) {
        try {
            EventoResponseDTO response = eventoService.crearEvento(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> actualizarEvento(@PathVariable String id,
            @Valid @RequestBody EventoRequestDTO dto) {
        try {
            EventoResponseDTO response = eventoService.actualizarEvento(id, dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable String id) {
        try {
            eventoService.eliminarEvento(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/publicar")
    public ResponseEntity<EventoResponseDTO> publicarEvento(@PathVariable String id) {
        try {
            EventoResponseDTO response = eventoService.publicarEvento(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/iniciar")
    public ResponseEntity<EventoResponseDTO> iniciarEvento(@PathVariable String id) {
        try {
            EventoResponseDTO response = eventoService.iniciarEvento(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<EventoResponseDTO> finalizarEvento(@PathVariable String id) {
        try {
            EventoResponseDTO response = eventoService.finalizarEvento(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin/{adminUid}")
    public ResponseEntity<List<EventoResponseDTO>> obtenerEventosAdmin(@PathVariable String adminUid) {
        try {
            List<EventoResponseDTO> eventos = eventoService.obtenerEventosPorAdmin(adminUid);
            return ResponseEntity.ok(eventos);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ==========================================
    // ENDPOINTS CLIENTE
    // ==========================================

    @GetMapping("/publicados")
    public ResponseEntity<List<EventoResponseDTO>> obtenerEventosPublicados() {
        try {
            List<EventoResponseDTO> eventos = eventoService.obtenerEventosPublicados();
            return ResponseEntity.ok(eventos);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> obtenerEventoPorId(@PathVariable String id) {
        try {
            EventoResponseDTO response = eventoService.obtenerEventoPorId(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
