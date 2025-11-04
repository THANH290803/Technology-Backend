# Stage 1: Build Gradle (multi-arch)
FROM gradle:8.10-jdk21 AS builder
WORKDIR /app
COPY . .
# Build JAR, bỏ test nếu cần
RUN gradle clean build -x test

# Stage 2: Run app với Temurin JDK 21 (ARM64 compatible)
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/Technology-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV PORT=8080
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --server.port=$PORT"]
