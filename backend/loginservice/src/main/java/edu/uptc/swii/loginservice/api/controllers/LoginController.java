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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://127.0.0.1:5500", 
             allowedHeaders = "*", 
             methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/createuser")
    public ResponseEntity<String> register(@Valid @RequestBody LoginRequestDTO dto) {
        loginService.register(dto);
        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/authuser")
    public LoginResponseDTO authenticate(@RequestBody LoginRequestDTO dto) {
        return loginService.authenticate(dto);
    }
}