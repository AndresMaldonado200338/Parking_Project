package edu.uptc.swii.loginservice.api.dtos;

import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "LoginRequest", description = "DTO para solicitudes de login o registro")
public class LoginRequestDTO {
    @NotNull
    @Schema(description = "Identificador numérico del usuario", example = "12345", required = true)
    private Long userID;

    @NotBlank
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Schema(description = "Contraseña del usuario (mínimo 8 caracteres)", example = "miClaveSegura123", required = true, minLength = 8)
    private String password;
}
