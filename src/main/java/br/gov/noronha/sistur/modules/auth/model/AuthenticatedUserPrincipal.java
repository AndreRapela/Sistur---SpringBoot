package br.gov.noronha.sistur.modules.auth.model;

import java.security.Principal;

public record AuthenticatedUserPrincipal(Long id, String email) implements Principal {
    @Override
    public String getName() {
        return email;
    }
}
