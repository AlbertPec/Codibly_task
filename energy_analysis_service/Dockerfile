FROM eclipse-temurin:17-jdk AS builder
WORKDIR /workspace
COPY . .
RUN ./gradlew build

FROM eclipse-temurin:17-jre
COPY --from=builder /workspace/build/libs/app.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]