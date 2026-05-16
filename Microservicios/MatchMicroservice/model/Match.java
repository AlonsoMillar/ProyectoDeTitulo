package com.example.matchmicroservice.model;

import lombok.Data;
import java.util.List;

@Data
public class Match {
    private String id;
    private String eventoId;
    private String storeCode;
    private String estado; // ESPERANDO, ACTIVA, FINALIZADA
    private String createdAt;
    private String startedAt;
    private String finishedAt;
    private int duracionMinutos;
    private String adminUid;
    private List<ParticipanteMatch> participantes;
}
