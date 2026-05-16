package com.example.participantemicroservice.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Participante {
    private String id;
    private String eventoId;
    private String eventoNombre;
    private String storeCode;
    private String userUid;
    private String alias;
    private String rolEvento; // JUGADOR, JUEZ
    private boolean ready;
    private String estado; // INSCRITO, READY, RETIRADO
    private String joinedAt; // ISO-8601 date string
}
