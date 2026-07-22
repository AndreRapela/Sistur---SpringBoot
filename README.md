# SisTur Backend

API Spring Boot do SisTur para turismo em Fernando de Noronha.

## Requisitos

- JDK 21
- Maven 3.9+ ou Docker
- Banco Postgres/Supabase ativo

## Variaveis obrigatorias

Configure no ambiente antes de iniciar:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=<usuario>
SPRING_DATASOURCE_PASSWORD=<senha>
JWT_SECRET=<segredo-forte-com-minimo-32-caracteres>
GOOGLE_CLIENT_ID=<client-id-google>
CORS_ALLOWED_ORIGINS=https://<dominio-do-frontend>
```

Opcional, quando o backend passar a validar tokens externos do Supabase diretamente:

```bash
SUPABASE_JWKS_URL=https://<projeto>.supabase.co/auth/v1/.well-known/jwks.json
```

## Clima e segurança

O backend centraliza e mantém em cache as consultas de previsão terrestre e marítima. Para desenvolvimento e avaliação:

```bash
OPEN_METEO_FORECAST_URL=https://api.open-meteo.com/v1/forecast
OPEN_METEO_MARINE_URL=https://marine-api.open-meteo.com/v1/marine
OPEN_METEO_API_KEY=
WEATHER_CACHE_MINUTES=10
```

Antes de um lançamento comercial, configure uma assinatura do provedor e mantenha a chave somente no backend:

```bash
OPEN_METEO_FORECAST_URL=https://customer-api.open-meteo.com/v1/forecast
OPEN_METEO_MARINE_URL=https://customer-marine-api.open-meteo.com/v1/marine
OPEN_METEO_API_KEY=<chave-comercial>
```

O seed turistico roda por padrao contra o banco configurado. Para desativar:

```bash
SISTUR_SEED_ENABLED=false
```

## Execucao

```bash
mvn spring-boot:run
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

## Observacoes de lancamento

- Use JDK 21 no IntelliJ. O projeto compila com `release 21`; JDK 26 pode quebrar processadores de anotacao.
- Nao deixe senha de banco ou `JWT_SECRET` versionados.
- Para Supabase, prefira URL com SSL habilitado.
- Configure `CORS_ALLOWED_ORIGINS` com o dominio real do frontend em producao.
