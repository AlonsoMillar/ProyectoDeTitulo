package com.example.matchmicroservice.service;

import com.example.matchmicroservice.dto.MatchDTO;
import com.example.matchmicroservice.model.Match;
import com.example.matchmicroservice.model.ParticipanteMatch;
import com.example.matchmicroservice.repository.MatchRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public Match crearMatch(MatchDTO dto) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        
        // Validate Evento
        DocumentReference eventoRef = db.collection("eventos").document(dto.getEventoId());
        DocumentSnapshot eventoDoc = eventoRef.get().get();

        if (!eventoDoc.exists()) {
            throw new Exception("El evento no existe");
        }
        if (!"PUBLICADO".equals(eventoDoc.getString("estado"))) {
            throw new Exception("El evento no está PUBLICADO");
        }

        // Validate Participantes
        CollectionReference participantesRef = db.collection("participantes");
        Query query = participantesRef.whereEqualTo("eventoId", dto.getEventoId());
        QuerySnapshot querySnapshot = query.get().get();

        List<com.google.cloud.firestore.QueryDocumentSnapshot> participantesDocs = querySnapshot.getDocuments();
        if (participantesDocs.isEmpty()) {
            throw new Exception("No existen participantes en este evento");
        }

        List<ParticipanteMatch> participantesMatch = new ArrayList<>();
        for (DocumentSnapshot doc : participantesDocs) {
            String estado = doc.getString("estado");
            String rol = doc.getString("rolEvento");
            Boolean ready = doc.getBoolean("ready");

            if ("RETIRADO".equals(estado)) {
                continue;
            }

            if ("JUGADOR".equals(rol) && (ready == null || !ready)) {
                throw new Exception("No se puede crear la partida: Todos los jugadores deben estar READY");
            }

            ParticipanteMatch pm = new ParticipanteMatch();
            pm.setUserUid(doc.getString("userUid"));
            pm.setAlias(doc.getString("alias"));
            pm.setRolEvento(rol);
            pm.setPuntos(0);
            pm.setReady(ready != null ? ready : false);
            pm.setActivo(true);
            participantesMatch.add(pm);
        }

        if (participantesMatch.isEmpty()) {
             throw new Exception("No hay participantes válidos para jugar");
        }

        Match match = new Match();
        match.setEventoId(dto.getEventoId());
        match.setStoreCode(eventoDoc.getString("storeCode"));
        match.setEstado("ESPERANDO");
        match.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        match.setDuracionMinutos(dto.getDuracionMinutos() > 0 ? dto.getDuracionMinutos() : 50); // default 50 min
        match.setAdminUid(dto.getAdminUid());
        match.setParticipantes(participantesMatch);

        matchRepository.saveMatch(match);
        return match;
    }

    public Match iniciarMatch(String id) throws Exception {
        Match m = matchRepository.getMatchById(id);
        if (m == null) throw new Exception("Partida no encontrada");
        if (!"ESPERANDO".equals(m.getEstado())) throw new Exception("La partida no está en estado ESPERANDO");

        m.setEstado("ACTIVA");
        m.setStartedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        matchRepository.saveMatch(m);
        return m;
    }

    public Match finalizarMatch(String id) throws Exception {
        Match m = matchRepository.getMatchById(id);
        if (m == null) throw new Exception("Partida no encontrada");

        m.setEstado("FINALIZADA");
        m.setFinishedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        matchRepository.saveMatch(m);
        return m;
    }

    public Match modificarPuntosAdmin(String id, String targetUid, int puntosDelta) throws Exception {
        Match m = matchRepository.getMatchById(id);
        if (m == null) throw new Exception("Partida no encontrada");
        if ("FINALIZADA".equals(m.getEstado())) throw new Exception("La partida ya finalizó, no se pueden editar puntos");

        boolean modificado = false;
        for (ParticipanteMatch pm : m.getParticipantes()) {
            if (pm.getUserUid().equals(targetUid)) {
                pm.setPuntos(pm.getPuntos() + puntosDelta);
                modificado = true;
                break;
            }
        }

        if (!modificado) throw new Exception("Jugador no encontrado en la partida");

        matchRepository.saveMatch(m);
        return m;
    }

    public Match modificarPuntosSelf(String id, String userUid, int puntosDelta) throws Exception {
        Match m = matchRepository.getMatchById(id);
        if (m == null) throw new Exception("Partida no encontrada");
        if ("FINALIZADA".equals(m.getEstado())) throw new Exception("La partida ya finalizó, no se pueden editar puntos");

        boolean modificado = false;
        for (ParticipanteMatch pm : m.getParticipantes()) {
            if (pm.getUserUid().equals(userUid)) {
                pm.setPuntos(pm.getPuntos() + puntosDelta);
                modificado = true;
                break;
            }
        }

        if (!modificado) throw new Exception("No estás participando en esta partida");

        matchRepository.saveMatch(m);
        return m;
    }

    public Match verPartida(String id) throws Exception {
        return matchRepository.getMatchById(id);
    }

    public List<Match> verPartidasUser(String uid) throws Exception {
        return matchRepository.getMatchesByUserUid(uid);
    }
}
