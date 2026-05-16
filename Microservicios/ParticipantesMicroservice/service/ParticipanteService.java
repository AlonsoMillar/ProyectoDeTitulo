package com.example.participantemicroservice.service;

import com.example.participantemicroservice.dto.ParticipanteDTO;
import com.example.participantemicroservice.model.Participante;
import com.example.participantemicroservice.repository.ParticipanteRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    public Participante unirseEvento(ParticipanteDTO dto) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference eventoRef = db.collection("eventos").document(dto.getEventoId());
        ApiFuture<DocumentSnapshot> future = eventoRef.get();
        DocumentSnapshot eventoDoc = future.get();

        if (!eventoDoc.exists()) {
            throw new Exception("El evento no existe");
        }

        String estadoEvento = eventoDoc.getString("estado");
        if (!"PUBLICADO".equals(estadoEvento)) {
            throw new Exception("El evento no está PUBLICADO. Estado actual: " + estadoEvento);
        }

        // Validar que el usuario no esté repetido
        List<Participante> participantesActuales = participanteRepository.getParticipantesByEventoId(dto.getEventoId());
        for (Participante p : participantesActuales) {
            if (p.getUserUid().equals(dto.getUserUid()) && !p.getEstado().equals("RETIRADO")) {
                throw new Exception("El usuario ya está inscrito en este evento");
            }
        }

        // Validar cupos
        Long maxJugadores = eventoDoc.getLong("maxJugadores");
        if (maxJugadores != null) {
            long inscritos = participantesActuales.stream().filter(p -> "JUGADOR".equals(p.getRolEvento()) && !"RETIRADO".equals(p.getEstado())).count();
            if ("JUGADOR".equals(dto.getRolEvento()) && inscritos >= maxJugadores) {
                throw new Exception("No hay cupos disponibles para jugadores");
            }
        }

        // Validar Juez
        if ("JUEZ".equals(dto.getRolEvento())) {
            Boolean requiereJuez = eventoDoc.getBoolean("requiereJuez");
            if (requiereJuez == null || !requiereJuez) {
                throw new Exception("Este evento no requiere juez");
            }
        }

        Participante participante = new Participante();
        participante.setEventoId(dto.getEventoId());
        participante.setEventoNombre(eventoDoc.getString("nombre")); // Extraer de evento
        participante.setStoreCode(eventoDoc.getString("storeCode")); // Extraer de evento
        participante.setUserUid(dto.getUserUid());
        participante.setAlias(dto.getAlias());
        participante.setRolEvento(dto.getRolEvento());
        participante.setReady(false);
        participante.setEstado("INSCRITO");
        participante.setJoinedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        participanteRepository.saveParticipante(participante);
        return participante;
    }

    public Participante markAsReady(String id) throws Exception {
        Participante p = participanteRepository.getParticipanteById(id);
        if (p == null) {
            throw new Exception("Participante no encontrado");
        }

        p.setReady(true);
        p.setEstado("READY");
        participanteRepository.saveParticipante(p);
        return p;
    }

    public boolean salirEvento(String id) throws Exception {
        Participante p = participanteRepository.getParticipanteById(id);
        if (p == null) {
            throw new Exception("Participante no encontrado");
        }

        Firestore db = FirestoreClient.getFirestore();
        DocumentReference eventoRef = db.collection("eventos").document(p.getEventoId());
        DocumentSnapshot eventoDoc = eventoRef.get().get();

        if (eventoDoc.exists()) {
            String estadoEvento = eventoDoc.getString("estado");
            if ("EN_CURSO".equals(estadoEvento)) {
                throw new Exception("No puedes salir porque el evento ya está EN_CURSO");
            }
        }

        p.setEstado("RETIRADO");
        p.setReady(false);
        participanteRepository.saveParticipante(p);
        return true;
    }

    public List<Participante> listarPorEvento(String eventoId) throws ExecutionException, InterruptedException {
        return participanteRepository.getParticipantesByEventoId(eventoId);
    }

    public List<Participante> listarReady(String eventoId) throws ExecutionException, InterruptedException {
        return participanteRepository.getParticipantesByEventoIdAndReady(eventoId, true);
    }

    public int contarInscritos(String eventoId) throws ExecutionException, InterruptedException {
        return (int) participanteRepository.getParticipantesByEventoId(eventoId).stream()
                .filter(p -> !"RETIRADO".equals(p.getEstado()))
                .count();
    }

    public void expulsar(String id) throws Exception {
        participanteRepository.deleteParticipante(id);
    }

    public List<Participante> listarPorUsuario(String uid) throws ExecutionException, InterruptedException {
        return participanteRepository.getParticipantesByUserUid(uid);
    }
}
