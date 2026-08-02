package br.gov.noronha.sistur.modules.auth.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.ProfileUpdateRequest;
import br.gov.noronha.sistur.dto.UserDTO;
import br.gov.noronha.sistur.modules.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success(
            userService.getProfile(authentication.getName()),
            "Perfil carregado"
        ));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(
        @Valid @RequestBody ProfileUpdateRequest request,
        Authentication authentication
    ) {
        return ResponseEntity.ok(ApiResponse.success(
            userService.updateProfile(authentication.getName(), request),
            "Perfil atualizado com sucesso"
        ));
    }
}
