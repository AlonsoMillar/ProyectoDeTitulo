package com.example.MessageMicroservice.Repository;

import com.example.MessageMicroservice.Model.Mensaje;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
public class MensajeRepository {

    // Guardar mensaje en "mensajes" y actualizar la carátula en "chats"
    public Mensaje guardarMensaje(Mensaje mensaje, String storeCode)
            throws ExecutionException, InterruptedException {

        Firestore db = FirestoreClient.getFirestore();

        // Generar ID único para el documento del mensaje
        String id = UUID.randomUUID().toString();
        mensaje.setId(id);

        // 1. Persistir el mensaje individual
        db.collection("mensajes")
                .document(id)
                .set(mensaje)
                .get();

        long now = mensaje.getCreatedAt();

        // 2. Preparar los datos para la colección "chats" (Bandeja de Entrada)
        Map<String, Object> chatData = new HashMap<>();
        chatData.put("chatId", mensaje.getChatId());
        chatData.put("storeCode", storeCode);
        chatData.put("lastMessage", mensaje.getContenido());
        chatData.put("lastTime", now);

        // REPLICACIÓN DEL ALIAS:
        // Si el rol es CLIENTE, guardamos su alias en el documento del chat.
        // Si es ADMIN, no sobreescribimos el nombre para que el Admin siga viendo al cliente.
        if ("CLIENTE".equalsIgnoreCase(mensaje.getSenderRole())) {
            chatData.put("senderAlias", mensaje.getSenderAlias());
        }

        // Actualizar el chat usando merge para no borrar el alias si el admin responde
        db.collection("chats")
                .document(mensaje.getChatId())
                .set(chatData, SetOptions.merge())
                .get();

        return mensaje;
    }

    // Obtener historial de mensajes de un chat específico
    public List<Mensaje> obtenerMensajesPorChat(String chatId)
            throws ExecutionException, InterruptedException {

        Firestore db = FirestoreClient.getFirestore();

        QuerySnapshot snapshot = db.collection("mensajes")
                .whereEqualTo("chatId", chatId)
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .get()
                .get();

        List<Mensaje> mensajes = new ArrayList<>();
        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            mensajes.add(doc.toObject(Mensaje.class));
        }

        return mensajes;
    }

    // Obtener lista de chats (Bandeja de entrada) para un storeCode
    public List<Map<String, Object>> obtenerChats(String storeCode)
            throws ExecutionException, InterruptedException {

        Firestore db = FirestoreClient.getFirestore();

        // IMPORTANTE: Esto requiere que tengas un índice compuesto en Firestore
        QuerySnapshot snapshot = db.collection("chats")
                .whereEqualTo("storeCode", storeCode)
                .orderBy("lastTime", Query.Direction.DESCENDING)
                .get()
                .get();

        List<Map<String, Object>> result = new ArrayList<>();
        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            result.add(doc.getData());
        }

        return result;
    }
}