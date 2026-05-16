package com.example.matchmicroservice.controller;

import com.example.matchmicroservice.dto.MatchDTO;
import com.example.matchmicroservice.dto.ScoreDTO;
import com.example.matchmicroservice.model.Match;
import com.example.matchmicroservice.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    // ADMIN/JUEZ
    @PostMapping
    public ResponseEntity<?> crearPartida(@Valid @RequestBody MatchDTO dto) {
        try {
            Match m = matchService.crearMatch(dto);
            return new ResponseEntity<>(m, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<?> iniciarPartida(@PathVariable String id) {
        try {
            Match m = matchService.iniciarMatch(id);
            return new ResponseEntity<>(m, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<?> finalizarPartida(@PathVariable String id) {
        try {
            Match m = matchService.finalizarMatch(id);
            return new ResponseEntity<>(m, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/score/{uid}")
    public ResponseEntity<?> modificarPuntosAdmin(@PathVariable String id, @PathVariable String uid, @RequestBody ScoreDTO dto) {
        try {
            Match m = matchService.modificarPuntosAdmin(id, uid, dto.getPuntos());
            return new ResponseEntity<>(m, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> verPartida(@PathVariable String id) {
        try {
            Match m = matchService.verPartida(id);
            if (m == null) return new ResponseEntity<>("No encontrada", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(m, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PARTICIPANTE
    @PostMapping("/{id}/self-score")
    public ResponseEntity<?> modificarPuntosPropios(@PathVariable String id, @RequestParam String uid, @RequestBody ScoreDTO dto) {
        try {
            Match m = matchService.modificarPuntosSelf(id, uid, dto.getPuntos());
            return new ResponseEntity<>(m, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<?> verPartidaUser(@PathVariable String uid) {
        try {
            List<Match> matches = matchService.verPartidasUser(uid);
            return new ResponseEntity<>(matches, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // TEST
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return new ResponseEntity<>("MatchMicroservice is up and running!", HttpStatus.OK);
    }
}
