# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio de Execução
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/sistur-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que o Render vai usar
EXPOSE 8080

# Comando para iniciar a aplicação mapeando a porta do Render e preferindo IPv4
ENTRYPOINT ["java", "-Djava.net.preferIPv4Stack=true", "-jar", "app.jar", "--server.port=${PORT:8080}"]
