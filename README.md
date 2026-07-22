# SisTur Backend

API Spring Boot do SisTur para turismo em Fernando de Noronha.

## Requisitos

- JDK 21
- Maven 3.9+ ou Docker
- Banco Postgres/Supabase ativo

## Variaveis obrigatorias

Configure no ambiente antes de iniciar:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://db.<project-ref>.supabase.co:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=<senha>
JWT_SECRET=<segredo-forte-com-minimo-32-caracteres>
GOOGLE_CLIENT_ID=<client-id-google>
CORS_ALLOWED_ORIGINS=https://<dominio-do-frontend>
```

Essa conexao direta e a melhor opcao para um backend persistente quando o
ambiente possui IPv6. Em uma rede somente IPv4, copie no painel do Supabase a
opcao `Connect > Session pooler` e use:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://aws-0-<regiao>.pooler.supabase.com:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres.<project-ref>
```

Nao use o Transaction pooler da porta `6543` para este backend persistente. O
host e a regiao devem sempre ser copiados do painel atual do projeto.

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

## Diagnostico do Supabase

Se a inicializacao terminar em `SocketTimeoutException: Connect timed out`, a
aplicacao ainda nao chegou a validar usuario ou senha. Confirme primeiro que o
projeto esta ativo e teste a URL configurada:

```powershell
Test-NetConnection db.<project-ref>.supabase.co -Port 5432
```

Quando `TcpTestSucceeded` for `False`, copie novamente a conexao em `Connect`
no painel do Supabase ou use o modo alternativo descrito acima. Uma senha
incorreta produz erro de autenticacao, nao timeout de socket.

## Observacoes de lancamento

- Use JDK 21 no IntelliJ. O projeto compila com `release 21`; JDK 26 pode quebrar processadores de anotacao.
- Nao deixe senha de banco ou `JWT_SECRET` versionados.
- Para Supabase, prefira URL com SSL habilitado.
- Configure `CORS_ALLOWED_ORIGINS` com o dominio real do frontend em producao.
- Referencia oficial de conexoes: https://supabase.com/docs/guides/database/connecting-to-postgres
