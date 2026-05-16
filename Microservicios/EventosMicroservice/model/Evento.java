package com.example.eventomicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {
    private String id;
    private String nombre;
    private String descripcion;
    private String fechaEvento;
    private Integer duracionMinutos;
    private Integer maxParticipantes;
    private boolean requiereJuez;
    private String storeCode;
    private String adminUid;
    private String adminAlias;
    private EstadoEvento estado;
    private Date createdAt;
}
