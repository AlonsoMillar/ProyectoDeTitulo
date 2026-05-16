package com.example.participantemicroservice.repository;

import com.example.participantemicroservice.model.Participante;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class ParticipanteRepository {

    private static final String COLLECTION_NAME = "participantes";

    public String saveParticipante(Participante participante) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef;

        if (participante.getId() == null || participante.getId().isEmpty()) {
            docRef = db.collection(COLLECTION_NAME).document();
            participante.setId(docRef.getId());
        } else {
            docRef = db.collection(COLLECTION_NAME).document(participante.getId());
        }

        ApiFuture<WriteResult> result = docRef.set(participante);
        result.get();
        return participante.getId();
    }

    public Participante getParticipanteById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.toObject(Participante.class);
        }
        return null;
    }

    public List<Participante> getParticipantesByEventoId(String eventoId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference participantes = db.collection(COLLECTION_NAME);
        Query query = participantes.whereEqualTo("eventoId", eventoId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        List<Participante> list = new ArrayList<>();
        for (DocumentSnapshot doc : querySnapshot.get().getDocuments()) {
            list.add(doc.toObject(Participante.class));
        }
        return list;
    }

    public List<Participante> getParticipantesByEventoIdAndReady(String eventoId, boolean ready) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference participantes = db.collection(COLLECTION_NAME);
        Query query = participantes.whereEqualTo("eventoId", eventoId).whereEqualTo("ready", ready);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        List<Participante> list = new ArrayList<>();
        for (DocumentSnapshot doc : querySnapshot.get().getDocuments()) {
            list.add(doc.toObject(Participante.class));
        }
        return list;
    }

    public List<Participante> getParticipantesByUserUid(String userUid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference participantes = db.collection(COLLECTION_NAME);
        Query query = participantes.whereEqualTo("userUid", userUid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        List<Participante> list = new ArrayList<>();
        for (DocumentSnapshot doc : querySnapshot.get().getDocuments()) {
            list.add(doc.toObject(Participante.class));
        }
        return list;
    }

    public boolean deleteParticipante(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection(COLLECTION_NAME).document(id).delete();
        writeResult.get();
        return true;
    }
}
