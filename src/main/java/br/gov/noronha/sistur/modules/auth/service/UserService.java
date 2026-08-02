package br.gov.noronha.sistur.modules.auth.service;

import br.gov.noronha.sistur.dto.ProfileUpdateRequest;
import br.gov.noronha.sistur.dto.UserDTO;
import br.gov.noronha.sistur.exception.ResourceNotFoundException;
import br.gov.noronha.sistur.modules.auth.model.User;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDTO getProfile(String email) {
        return toDTO(requireUser(email));
    }

    @Transactional
    public UserDTO updateProfile(String email, ProfileUpdateRequest request) {
        User user = requireUser(email);
        user.setName(request.name().trim());
        user.setBio(blankToNull(request.bio()));
        user.setPhotoUrl(blankToNull(request.photoUrl()));
        return toDTO(userRepository.save(user));
    }

    private User requireUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhotoUrl(),
            user.getBio(),
            user.getRole(),
            user.getOwnedEstablishmentId()
        );
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
