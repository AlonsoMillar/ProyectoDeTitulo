package com.example.eventomicroservice.dto;

import com.example.eventomicroservice.model.EstadoEvento;
import lombok.Data;

import java.util.Date;

@Data
public class EventoResponseDTO {
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
