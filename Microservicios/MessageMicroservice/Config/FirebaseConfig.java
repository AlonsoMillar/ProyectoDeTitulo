package com.example.MessageMicroservice.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {

        try {

            if (FirebaseApp.getApps().isEmpty()) {

                InputStream serviceAccount = getClass()
                        .getClassLoader()
                        .getResourceAsStream("firebase-service-account.json");

                if (serviceAccount == null) {
                    throw new RuntimeException("No se encontró firebase-service-account.json");
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);

                System.out.println(" Firebase inicializado correctamente");
            }

        } catch (Exception e) {
            System.out.println(" Error inicializando Firebase");
            e.printStackTrace();
        }
    }

    //  MÉTODO DE VERIFICACIÓN (para /test)
    public static boolean isFirebaseReady() {

        try {
            Firestore db = FirestoreClient.getFirestore();

            // prueba simple: no rompe nada
            db.collection("_healthcheck").document("ping").get();

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}