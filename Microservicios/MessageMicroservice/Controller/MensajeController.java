package com.example.MessageMicroservice.Controller;

import com.example.MessageMicroservice.DTO.MensajeRequest;
import com.example.MessageMicroservice.Model.Mensaje;
import com.example.MessageMicroservice.Service.MensajeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mensajes")
@CrossOrigin("*")
public class MensajeController {

    private final MensajeService service;

    public MensajeController(MensajeService service) {
        this.service = service;
    }

    // CORE ENDPOINTS (CHAT FUNCIONAL)

    @PostMapping("/enviar")
    public ResponseEntity<?> enviar(@RequestBody MensajeRequest r) {
        try {
            return ResponseEntity.ok(service.enviarMensaje(r));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<?> chat(@PathVariable String chatId) {
        return ResponseEntity.ok(service.obtenerMensajes(chatId));
    }

    @GetMapping("/chats/{storeCode}")
    public ResponseEntity<?> chats(@PathVariable String storeCode) {
        return ResponseEntity.ok(service.obtenerChats(storeCode));
    }

    // TESTS / DEBUG ENDPOINTS

    // 1. Health check del microservicio
    @GetMapping("/test")
    public ResponseEntity<String> testService() {
        return ResponseEntity.ok("Microservicio OK - Mensajes activo");
    }

    // 2. Test de escritura rápida
    @PostMapping("/test/write")
    public ResponseEntity<?> testWrite(@RequestBody MensajeRequest r) {
        try {
            r.setContenido("TEST MESSAGE - " + System.currentTimeMillis());
            Mensaje m = service.enviarMensaje(r);
            return ResponseEntity.ok(m);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error test write: " + e.getMessage());
        }
    }

    // 3. Test de lectura de chat
    @GetMapping("/test/chat/{chatId}")
    public ResponseEntity<?> testChat(@PathVariable String chatId) {
        try {
            List<Mensaje> mensajes = service.obtenerMensajes(chatId);

            return ResponseEntity.ok(
                    Map.of(
                            "chatId", chatId,
                            "count", mensajes.size(),
                            "messages", mensajes
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error test chat: " + e.getMessage());
        }
    }

    // 4. Test inbox admin
    @GetMapping("/test/chats/{storeCode}")
    public ResponseEntity<?> testChats(@PathVariable String storeCode) {
        try {
            List<Map<String, Object>> chats = service.obtenerChats(storeCode);

            return ResponseEntity.ok(
                    Map.of(
                            "storeCode", storeCode,
                            "count", chats.size(),
                            "chats", chats
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error test chats: " + e.getMessage());
        }
    }
}