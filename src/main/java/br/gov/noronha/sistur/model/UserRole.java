package br.gov.noronha.sistur.model;

public enum UserRole {
    ADMIN,    // Acesso total
    CLIENT,   // Dono de negócio (pode gerenciar o próprio estabelecimento e ver métricas)
    USER      // Turista (acesso comum)
}
