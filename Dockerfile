FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY energy_analysis_service/ .

RUN chmod +x gradlew

RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
