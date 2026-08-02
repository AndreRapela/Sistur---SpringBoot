FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn --batch-mode --no-transfer-progress dependency:go-offline

COPY src ./src
RUN mvn --batch-mode --no-transfer-progress clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN groupadd --system sistur && useradd --system --gid sistur --home-dir /app sistur
COPY --from=build --chown=sistur:sistur /app/target/sistur-0.0.1-SNAPSHOT.jar app.jar

USER sistur
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java -Djava.net.preferIPv4Stack=true -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError -jar app.jar --server.port=${PORT:-8080}"]
