package com.example.matchmicroservice.model;

import lombok.Data;

@Data
public class ParticipanteMatch {
    private String userUid;
    private String alias;
    private String rolEvento;
    private int puntos;
    private boolean ready;
    private boolean activo;
}
