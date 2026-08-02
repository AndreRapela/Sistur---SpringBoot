package br.gov.noronha.sistur.modules.auth.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.LoginRequestDTO;
import br.gov.noronha.sistur.dto.LoginResponseDTO;
import br.gov.noronha.sistur.exception.ConflictException;
import br.gov.noronha.sistur.exception.UnauthenticatedException;
import br.gov.noronha.sistur.modules.auth.model.User;
import br.gov.noronha.sistur.modules.auth.model.UserRole;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import br.gov.noronha.sistur.modules.auth.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @org.springframework.beans.factory.annotation.Value("${google.client.id}")
    private String googleClientId;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO data) {
        String email = normalizeEmail(data.email());
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null && passwordEncoder.matches(data.password(), user.getPassword())) {
            String token = tokenService.generateToken(user.getEmail(), user.getRole().name());
            LoginResponseDTO response = LoginResponseDTO.fromUser(token, user);
            return ResponseEntity.ok(ApiResponse.success(response, "Login realizado com sucesso"));
        }

        throw new UnauthenticatedException("E-mail ou senha inválidos.");
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> register(@Valid @RequestBody br.gov.noronha.sistur.dto.RegisterRequestDTO data) {
        String email = normalizeEmail(data.email());
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("E-mail já cadastrado.");
        }

        User user = new User();
        user.setEmail(email);
        user.setName(data.name().trim());
        user.setPassword(passwordEncoder.encode(data.password()));
        user.setRole(resolveRegisterRole());
        
        userRepository.save(user);

        String token = tokenService.generateToken(user.getEmail(), user.getRole().name());
        LoginResponseDTO response = LoginResponseDTO.fromUser(token, user);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
            .body(ApiResponse.success(response, "Conta criada com sucesso"));
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> googleLogin(@RequestBody String idTokenBody) {
        if (idTokenBody != null && idTokenBody.length() > 20_000) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "Token do Google acima do limite permitido.");
        }
        if (googleClientId == null || googleClientId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Login Google não configurado.");
        }

        String idToken;
        try {
            idToken = extractGoogleIdToken(idTokenBody);
        } catch (com.fasterxml.jackson.core.JsonProcessingException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Corpo do token Google inválido.");
        }
        if (idToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token do Google ausente.");
        }

        com.google.api.client.googleapis.auth.oauth2.GoogleIdToken googleIdToken;
        try {
            com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier verifier = 
                new com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier.Builder(
                    new com.google.api.client.http.javanet.NetHttpTransport(), 
                    new com.google.api.client.json.gson.GsonFactory())
                .setAudience(java.util.Collections.singletonList(googleClientId))
                .build();
            googleIdToken = verifier.verify(idToken);
        } catch (Exception e) {
            log.warn("Falha ao validar login Google: {}", e.getClass().getSimpleName());
            throw new UnauthenticatedException("Não foi possível validar a conta Google.");
        }
        if (googleIdToken == null) {
            throw new UnauthenticatedException("Token do Google inválido.");
        }

        com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload payload = googleIdToken.getPayload();
        if (!Boolean.TRUE.equals(payload.getEmailVerified()) || payload.getEmail() == null || payload.getEmail().isBlank()) {
            throw new UnauthenticatedException("O e-mail da conta Google não está verificado.");
        }

        String email = normalizeEmail(payload.getEmail());
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name == null || name.isBlank() ? email.substring(0, email.indexOf('@')) : name.trim());
            newUser.setPhotoUrl(pictureUrl);
            newUser.setRole(br.gov.noronha.sistur.modules.auth.model.UserRole.FREE_TOURIST);
            newUser.setPassword(passwordEncoder.encode(java.util.UUID.randomUUID().toString()));
            return userRepository.save(newUser);
        });

        String token = tokenService.generateToken(user.getEmail(), user.getRole().name());
        LoginResponseDTO response = LoginResponseDTO.fromUser(token, user);
        return ResponseEntity.ok(ApiResponse.success(response, "Login Google realizado com sucesso"));
    }

    private String extractGoogleIdToken(String idTokenBody) throws com.fasterxml.jackson.core.JsonProcessingException {
        if (idTokenBody == null) {
            return "";
        }

        String body = idTokenBody.trim();
        if (body.startsWith("{")) {
            com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(body);
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

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
