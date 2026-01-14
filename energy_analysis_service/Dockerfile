FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew build

FROM eclipse-temurin:17-jre
COPY --from=builder /workspace/build/libs/energy_analysis_service-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]