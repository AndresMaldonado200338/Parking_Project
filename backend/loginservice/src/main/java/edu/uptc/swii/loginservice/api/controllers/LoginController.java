package edu.uptc.swii.loginservice.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.uptc.swii.loginservice.api.dtos.LoginRequestDTO;
import edu.uptc.swii.loginservice.api.dtos.LoginResponseDTO;
import edu.uptc.swii.loginservice.application.services.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
@Tag(name = "Login Service", description = "Microservicio para registro y autenticación de usuarios mediante JWT")
@CrossOrigin(origins = "http://127.0.0.1:5500", 
             allowedHeaders = "*", 
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario con userID y contraseña")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully", content = @Content(schema = @Schema(implementation = LoginRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (campos vacíos, contraseña menor a 8 caracteres)", content = @Content()),
            @ApiResponse(responseCode = "403", description = "userID no es numérico o ya existe", content = @Content())
    })
    @PostMapping("/createuser")
    public ResponseEntity<String> register(@Valid @RequestBody LoginRequestDTO dto) {
        loginService.register(dto);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "Autenticar usuario", description = "Autentica un usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas", content = @Content()),
            @ApiResponse(responseCode = "403", description = "userID no es numérico", content = @Content())})
    @PostMapping("/authuser")
    public LoginResponseDTO authenticate(@RequestBody LoginRequestDTO dto) {
        return loginService.authenticate(dto);
    }
}