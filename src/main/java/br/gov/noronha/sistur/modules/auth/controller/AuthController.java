package br.gov.noronha.sistur.modules.auth.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.LoginRequestDTO;
import br.gov.noronha.sistur.dto.LoginResponseDTO;
import br.gov.noronha.sistur.modules.auth.model.User;
import br.gov.noronha.sistur.modules.auth.model.UserRole;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import br.gov.noronha.sistur.modules.auth.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final com.fasterxml.jackson.databind.ObjectMapper JSON_MAPPER = new com.fasterxml.jackson.databind.ObjectMapper();

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Value("${google.client.id}")
    private String googleClientId;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO data) {
        User user = userRepository.findByEmail(data.email()).orElse(null);

        if (user != null && passwordEncoder.matches(data.password(), user.getPassword())) {
            String token = tokenService.generateToken(user.getEmail(), user.getRole().name());
            LoginResponseDTO response = new LoginResponseDTO(token, user.getName(), user.getEmail(), user.getRole());
            return ResponseEntity.ok(ApiResponse.success(response, "Login realizado com sucesso"));
        }

        return ResponseEntity.status(401).body(ApiResponse.error("E-mail ou senha inválidos"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> register(@Valid @RequestBody br.gov.noronha.sistur.dto.RegisterRequestDTO data) {
        if (userRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.status(400).body(ApiResponse.error("E-mail já cadastrado"));
        }

        User user = new User();
        user.setEmail(data.email());
        user.setName(data.name());
        user.setPassword(passwordEncoder.encode(data.password()));
        user.setRole(resolveRegisterRole());
        
        userRepository.save(user);

        String token = tokenService.generateToken(user.getEmail(), user.getRole().name());
        LoginResponseDTO response = new LoginResponseDTO(token, user.getName(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(ApiResponse.success(response, "Conta criada com sucesso"));
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> googleLogin(@RequestBody String idTokenBody) {
        try {
            if (googleClientId == null || googleClientId.isBlank()) {
                return ResponseEntity.status(503).body(ApiResponse.error("Login Google nao configurado"));
            }

            String idToken = extractGoogleIdToken(idTokenBody);
            if (idToken.isBlank()) {
                return ResponseEntity.status(400).body(ApiResponse.error("Token do Google ausente"));
            }

            com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier verifier = 
                new com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier.Builder(
                    new com.google.api.client.http.javanet.NetHttpTransport(), 
                    new com.google.api.client.json.gson.GsonFactory())
                .setAudience(java.util.Collections.singletonList(googleClientId))
                .build();

            com.google.api.client.googleapis.auth.oauth2.GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken != null) {
                com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload payload = googleIdToken.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");

                User user = userRepository.findByEmail(email).orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setPhotoUrl(pictureUrl);
                    newUser.setRole(br.gov.noronha.sistur.modules.auth.model.UserRole.FREE_TOURIST);
                    newUser.setPassword(passwordEncoder.encode(java.util.UUID.randomUUID().toString()));
                    return userRepository.save(newUser);
                });

                String token = tokenService.generateToken(user.getEmail(), user.getRole().name());
                LoginResponseDTO response = new LoginResponseDTO(token, user.getName(), user.getEmail(), user.getRole());
                return ResponseEntity.ok(ApiResponse.success(response, "Login Google realizado com sucesso"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(ApiResponse.error("Erro na autenticacao do Google"));
        }
        return ResponseEntity.status(401).body(ApiResponse.error("Token do Google invalido"));
    }

    private String extractGoogleIdToken(String idTokenBody) throws com.fasterxml.jackson.core.JsonProcessingException {
        if (idTokenBody == null) {
            return "";
        }

        String body = idTokenBody.trim();
        if (body.startsWith("{")) {
            com.fasterxml.jackson.databind.JsonNode node = JSON_MAPPER.readTree(body);
            for (String field : java.util.List.of("credential", "idToken", "token")) {
                String value = node.path(field).asText("");
                if (!value.isBlank()) {
                    return value.trim();
                }
            }
            return "";
        }

        if (body.startsWith("\"") && body.endsWith("\"")) {
            return body.substring(1, body.length() - 1).trim();
        }

        return body;
    }

    private UserRole resolveRegisterRole() {
        return UserRole.FREE_TOURIST;
    }
}
