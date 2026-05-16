package com.example.participantemicroservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ParticipanteDTO {

    @NotBlank(message = "El ID del evento es obligatorio")
    private String eventoId;

    @NotBlank(message = "El UID del usuario es obligatorio")
    private String userUid;

    @NotBlank(message = "El alias es obligatorio")
    private String alias;

    @NotBlank(message = "El rol en el evento es obligatorio")
    private String rolEvento;
    
    // Optional, can be resolved internally or passed by client
    private String eventoNombre;
    private String storeCode;
}
