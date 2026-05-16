package com.example.matchmicroservice.repository;

import com.example.matchmicroservice.model.Match;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class MatchRepository {

    private static final String COLLECTION_NAME = "matches";

    public String saveMatch(Match match) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef;

        if (match.getId() == null || match.getId().isEmpty()) {
            docRef = db.collection(COLLECTION_NAME).document();
            match.setId(docRef.getId());
        } else {
            docRef = db.collection(COLLECTION_NAME).document(match.getId());
        }

        ApiFuture<WriteResult> result = docRef.set(match);
        result.get();
        return match.getId();
    }

    public Match getMatchById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.toObject(Match.class);
        }
        return null;
    }

    public List<Match> getMatchesByEventoId(String eventoId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference matches = db.collection(COLLECTION_NAME);
        Query query = matches.whereEqualTo("eventoId", eventoId);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        List<Match> list = new ArrayList<>();
        for (DocumentSnapshot doc : querySnapshot.get().getDocuments()) {
            list.add(doc.toObject(Match.class));
        }
        return list;
    }

    public List<Match> getMatchesByUserUid(String userUid) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference matches = db.collection(COLLECTION_NAME);
        // Firestore queries on array of objects can be complex, usually we fetch and filter or maintain a separate index.
        // For simplicity, we fetch all matches and filter in memory, or we can use array-contains if we stored simple UIDs.
        // Assuming we store UIDs in a simple array field if needed, but here we filter in memory since we don't have a simple array.
        ApiFuture<QuerySnapshot> querySnapshot = matches.get();
        
        List<Match> list = new ArrayList<>();
        for (DocumentSnapshot doc : querySnapshot.get().getDocuments()) {
            Match m = doc.toObject(Match.class);
            if (m.getParticipantes() != null) {
                boolean containsUser = m.getParticipantes().stream().anyMatch(p -> p.getUserUid().equals(userUid));
                if (containsUser) {
                    list.add(m);
                }
            }
        }
        return list;
    }
}
