package com.example.eventomicroservice.repository;

import com.example.eventomicroservice.model.Evento;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class EventoRepository {

    private static final String COLLECTION_NAME = "eventos";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    public Evento save(Evento evento) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        if (evento.getId() == null || evento.getId().isEmpty()) {
            DocumentReference docRef = db.collection(COLLECTION_NAME).document();
            evento.setId(docRef.getId());
            ApiFuture<WriteResult> apiFuture = docRef.set(evento);
            apiFuture.get();
        } else {
            ApiFuture<WriteResult> apiFuture = db.collection(COLLECTION_NAME).document(evento.getId()).set(evento);
            apiFuture.get();
        }
        return evento;
    }

    public Evento findById(String id) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            return document.toObject(Evento.class);
        }
        return null;
    }

    public void delete(String id) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        ApiFuture<WriteResult> writeResult = db.collection(COLLECTION_NAME).document(id).delete();
        writeResult.get();
    }

    public List<Evento> findAll() throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Evento> eventos = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            eventos.add(document.toObject(Evento.class));
        }
        return eventos;
    }

    public List<Evento> findByAdminUid(String adminUid) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        CollectionReference eventosRef = db.collection(COLLECTION_NAME);
        Query query = eventosRef.whereEqualTo("adminUid", adminUid);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<Evento> eventos = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            eventos.add(document.toObject(Evento.class));
        }
        return eventos;
    }
    
    public List<Evento> findByEstado(String estado) throws ExecutionException, InterruptedException {
        Firestore db = getFirestore();
        CollectionReference eventosRef = db.collection(COLLECTION_NAME);
        Query query = eventosRef.whereEqualTo("estado", estado);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<Evento> eventos = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            eventos.add(document.toObject(Evento.class));
        }
        return eventos;
    }
}
