# ⚙️ SisTur - Backend (Spring Boot 3)

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/)
[![Java](https://img.shields.io/badge/Java%2022-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Security](https://img.shields.io/badge/JWT-Security-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)

> O motor de inteligência e segurança do SisTur, construído sobre uma arquitetura robusta e escalável em Spring Boot e PostgreSQL.

---

## 🚀 Funcionalidades Principais

- **Segurança Robusta**: Autenticação via JWT (Stateless) com RBAC (Role-Based Access Control).
- **Gestão de Turismo**: APIs completas para Restaurantes, Hotéis e Eventos locais.
- **Integração Supabase**: Banco de Dados PostgreSQL hospedado para máxima confiabilidade.
- **Tratamento Global de Erros**: Respostas Padronizadas e Toast-friendly para o frontend.
- **Documentação Interativa**: Swagger UI para testes rápidos e documentação técnica.

---

## 🛠️ Stack Tecnológica

- **Java 22**: Aproveitando as últimas melhorias da linguagem.
- **Spring Framework**:
    - Spring Data JPA (PostgreSQL)
    - Spring Security (JWT)
    - Spring Web (REST Controllers)
- **Banco de Dados**: PostgreSQL (Supabase)
- **Pool de Conexões**: HikariCP para máxima performance.

---

## 🏁 Como Rodar o Backend

### Requisitos
- JDK 22
- Maven 3.9+

### Configuração
As credenciais do Supabase devem ser configuradas no arquivo `src/main/resources/application.properties` ou via variáveis de ambiente.

### Execução
```bash
cd backend
mvn spring-boot:run
```

O servidor iniciará na porta **8080**.

---

## 📖 Documentação da API

Após iniciar o servidor, a documentação Swagger estará disponível em:
`http://localhost:8080/swagger-ui/index.html`

### Principais Endpoints
- `POST /auth/login`: Autenticação e recebimento de Token JWT.
- `GET /establishments`: Listagem de restaurantes e hotéis (filtrável).
- `GET /events`: Agenda de eventos interativa.
- `GET /itinerary`: Consultas de rotas e planejamentos.

---

**SisTur - Backend Inteligente para Turismo.**
