package edu.uptc.swii.loginservice.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "LoginResponse", description = "DTO para respuestas de autenticación")
public class LoginResponseDTO {
    @Schema(description = "Mensaje de respuesta", example = "Login successful")
    private String message;

    @Schema(description = "Token JWT generado", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}