package com.example.matchmicroservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MatchDTO {
    @NotBlank(message = "El evento ID es obligatorio")
    private String eventoId;
    
    @NotBlank(message = "El UID del admin es obligatorio")
    private String adminUid;
    
    private int duracionMinutos;
}
