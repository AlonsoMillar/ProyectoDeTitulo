package com.example.InicioSesion.Repository;

import com.example.InicioSesion.Model.Usuario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class UsuarioRepository {

    public String saveUsuario(Usuario usuario) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        
        // Guardamos en la colección "usuarios" usando el UID de Firebase como llave
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore
                .collection("usuarios")
                .document(usuario.getUid())
                .set(usuario);
        
        return collectionsApiFuture.get().getUpdateTime().toString();
    }
    public Usuario getUsuario(String uid) throws ExecutionException, InterruptedException {
    Firestore dbFirestore = FirestoreClient.getFirestore();

    DocumentSnapshot document = dbFirestore
            .collection("usuarios")
            .document(uid)
            .get()
            .get();

    if (document.exists()) {
        return document.toObject(Usuario.class);
    }

    return null;
}
}   
