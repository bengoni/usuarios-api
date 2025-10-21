# ---- Build (Maven + JDK 17) ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# ---- Run (JRE 17) ----
FROM eclipse-temurin:17-jre
WORKDIR /app
ENV PORT=8080
# Si tu jar NO termina en -SNAPSHOT.jar, cambia el patrón por el nombre real
COPY --from=build /app/target/*-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Dserver.port=${PORT}","-jar","/app/app.jar"]
