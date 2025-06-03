package edu.uptc.swii.loginservice.application.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.uptc.swii.loginservice.api.dtos.LoginRequestDTO;
import edu.uptc.swii.loginservice.api.dtos.LoginResponseDTO;
import edu.uptc.swii.loginservice.domain.models.Login;
import edu.uptc.swii.loginservice.domain.repositories.LoginRepository;
import edu.uptc.swii.loginservice.infraestructure.config.JwtUtil;

@Service
public class LoginService {

        @Autowired
        private LoginRepository loginRepository;

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private PasswordEncoder passwordEncoder;

        public void register(LoginRequestDTO dto) {
                Optional<Login> existingUser = loginRepository.findByUserID(dto.getUserID());
                if (existingUser.isPresent()) {
                        throw new RuntimeException("User with this ID already exists");
                }

                Login login = new Login();
                login.setUserID(dto.getUserID());
                login.setPassword(passwordEncoder.encode(dto.getPassword()));
                loginRepository.save(login);
        }

        public LoginResponseDTO authenticate(LoginRequestDTO dto) {
                Optional<Login> user = loginRepository.findByUserID(dto.getUserID());
                if (user.isPresent() && passwordEncoder.matches(dto.getPassword(), user.get().getPassword())) {
                        String token = jwtUtil.generateToken(user.get().getUserID());
                        return new LoginResponseDTO("Login successful", token);
                } else {
                        return new LoginResponseDTO("Invalid credentials", null);
                }
        }
}
