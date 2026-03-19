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
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Value("${google.client.id}")
    private String googleClientId;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@RequestBody LoginRequestDTO data) {
        User user = userRepository.findByEmail(data.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (passwordEncoder.matches(data.password(), user.getPassword())) {
            String token = tokenService.generateToken(user.getEmail(), user.getRole().name());
            LoginResponseDTO response = new LoginResponseDTO(token, user.getName(), user.getEmail(), user.getRole());
            return ResponseEntity.ok(ApiResponse.success(response, "Login realizado com sucesso"));
        }

        return ResponseEntity.status(401).body(ApiResponse.error("Senha inválida"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> register(@RequestBody br.gov.noronha.sistur.dto.RegisterRequestDTO data) {
        if (userRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.status(400).body(ApiResponse.error("E-mail já cadastrado"));
        }

        User user = new User();
        user.setEmail(data.email());
        user.setName(data.name());
        user.setPassword(passwordEncoder.encode(data.password()));
        user.setRole(br.gov.noronha.sistur.model.UserRole.USER);
        
        userRepository.save(user);

        String token = tokenService.generateToken(user.getEmail(), user.getRole().name());
        LoginResponseDTO response = new LoginResponseDTO(token, user.getName(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(ApiResponse.success(response, "Conta criada com sucesso"));
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> googleLogin(@RequestBody String idTokenBody) {
        try {
            // O frontend muitas vezes manda o token como string pura ou em um JSON
            String idToken = idTokenBody.contains(":") ? 
                idTokenBody.split(":")[1].replaceAll("[\"{}]", "").trim() : idTokenBody;

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
                    newUser.setRole(br.gov.noronha.sistur.model.UserRole.CLIENT);
                    newUser.setPassword(java.util.UUID.randomUUID().toString()); // Senha aleatória para social login
                    return userRepository.save(newUser);
                });

                String token = tokenService.generateToken(user.getEmail(), user.getRole().name());
                LoginResponseDTO response = new LoginResponseDTO(token, user.getName(), user.getEmail(), user.getRole());
                return ResponseEntity.ok(ApiResponse.success(response, "Login Google realizado com sucesso"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(ApiResponse.error("Erro na autenticação do Google: " + e.getMessage()));
        }
        return ResponseEntity.status(401).body(ApiResponse.error("Token do Google inválido"));
    }
}
