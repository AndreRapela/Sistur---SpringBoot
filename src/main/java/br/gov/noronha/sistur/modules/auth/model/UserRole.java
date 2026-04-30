package br.gov.noronha.sistur.modules.auth.model;

public enum UserRole {
    ADMIN,           // Acesso total
    CLIENT,          // Dono de negócio
    FREE_TOURIST,    // Turista gratuito
    USER,            // Turista Gratuito
    PRO_TOURIST,     // Turista Pro (vê eventos em tempo real)
    PREMIUM_TOURIST  // Turista Premium (vê eventos + descontos exclusivos)
}
