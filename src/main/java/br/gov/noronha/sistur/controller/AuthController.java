package br.gov.noronha.sistur.controller;

import br.gov.noronha.sistur.config.TokenService;
import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.LoginRequestDTO;
import br.gov.noronha.sistur.dto.LoginResponseDTO;
import br.gov.noronha.sistur.model.User;
import br.gov.noronha.sistur.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO data) {
        User user = userRepository.findByEmail(data.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Simplificado para desenvolvimento: comparar texto puro
        // Em produção usar passwordEncoder.matches(data.password(), user.getPassword())
        if (data.password().equals(user.getPassword())) {
            String token = tokenService.generateToken(user.getEmail(), user.getRole().name());
            LoginResponseDTO response = new LoginResponseDTO(token, user.getName(), user.getEmail(), user.getRole());
            return ResponseEntity.ok(ApiResponse.success(response, "Login realizado com sucesso"));
        }

        return ResponseEntity.status(401).body(ApiResponse.error("Senha inválida"));
    }
}
