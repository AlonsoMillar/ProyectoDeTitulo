package com.example.participantemicroservice.controller;

import com.example.participantemicroservice.dto.ParticipanteDTO;
import com.example.participantemicroservice.model.Participante;
import com.example.participantemicroservice.service.ParticipanteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participantes")
public class ParticipanteController {

    @Autowired
    private ParticipanteService participanteService;

    // CLIENTE
    @PostMapping("/unirse")
    public ResponseEntity<?> unirse(@Valid @RequestBody ParticipanteDTO dto) {
        try {
            Participante p = participanteService.unirseEvento(dto);
            return new ResponseEntity<>(p, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/ready")
    public ResponseEntity<?> markAsReady(@PathVariable String id) {
        try {
            Participante p = participanteService.markAsReady(id);
            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}/salir")
    public ResponseEntity<?> salirEvento(@PathVariable String id) {
        try {
            participanteService.salirEvento(id);
            return new ResponseEntity<>("Has salido del evento", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable String uid) {
        try {
            List<Participante> lista = participanteService.listarPorUsuario(uid);
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ADMIN
    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<?> listarPorEvento(@PathVariable String eventoId) {
        try {
            List<Participante> lista = participanteService.listarPorEvento(eventoId);
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/evento/{eventoId}/ready")
    public ResponseEntity<?> listarReady(@PathVariable String eventoId) {
        try {
            List<Participante> lista = participanteService.listarReady(eventoId);
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/evento/{eventoId}/count")
    public ResponseEntity<?> contarInscritos(@PathVariable String eventoId) {
        try {
            int count = participanteService.contarInscritos(eventoId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> expulsar(@PathVariable String id) {
        try {
            participanteService.expulsar(id);
            return new ResponseEntity<>("Participante expulsado correctamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // TEST
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return new ResponseEntity<>("ParticipanteMicroservice is up and running!", HttpStatus.OK);
    }
}
