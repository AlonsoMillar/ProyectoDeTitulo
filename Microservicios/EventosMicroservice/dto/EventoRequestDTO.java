package com.example.eventomicroservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "La fecha del evento es obligatoria")
    private String fechaEvento;

    @NotNull(message = "La duración es obligatoria")
    private Integer duracionMinutos;

    @NotNull(message = "El máximo de participantes es obligatorio")
    private Integer maxParticipantes;

    private boolean requiereJuez;

    @NotBlank(message = "El código de la tienda es obligatorio")
    private String storeCode;

    @NotBlank(message = "El UID del admin es obligatorio")
    private String adminUid;

    @NotBlank(message = "El alias del admin es obligatorio")
    private String adminAlias;
}
