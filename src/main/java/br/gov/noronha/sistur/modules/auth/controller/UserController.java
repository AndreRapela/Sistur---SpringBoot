package br.gov.noronha.sistur.modules.auth.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.UserDTO;
import br.gov.noronha.sistur.modules.auth.model.User;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        UserDTO dto = new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhotoUrl(),
            user.getBio(),
            user.getRole(),
            user.getOwnedEstablishmentId()
        );
        
        return ResponseEntity.ok(ApiResponse.success(dto, "Perfil carregado"));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(@RequestBody UserDTO data, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        user.setName(data.name());
        user.setBio(data.bio());
        user.setPhotoUrl(data.photoUrl());
        if (data.ownedEstablishmentId() != null) {
            user.setOwnedEstablishmentId(data.ownedEstablishmentId());
        }
        
        User updated = userRepository.save(user);
        
        UserDTO response = new UserDTO(
            updated.getId(),
            updated.getName(),
            updated.getEmail(),
            updated.getPhotoUrl(),
            updated.getBio(),
            updated.getRole(),
            updated.getOwnedEstablishmentId()
        );
        
        return ResponseEntity.ok(ApiResponse.success(response, "Perfil atualizado com sucesso"));
    }
}
