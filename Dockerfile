# ---- Build stage ----
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw.cmd mvnw.cmd
COPY pom.xml pom.xml
COPY src src

# Make the wrapper executable (Linux container)
RUN chmod +x mvnw.cmd || true
# Use Maven wrapper to build
RUN apk add --no-cache bash && \
    ./mvnw.cmd -Dmaven.multiModuleProjectDirectory=/app clean package -DskipTests

# ---- Run stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/algebra-solver-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
