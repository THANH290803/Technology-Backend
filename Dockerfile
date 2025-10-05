# Stage 1: Build Gradle (multi-arch)
FROM gradle:8.3.3-jdk21 AS builder
WORKDIR /app
COPY . .
# Build JAR, bỏ test nếu cần
RUN gradle clean build -x test

# Stage 2: Run app với OpenJDK 21.0.8
FROM openjdk:21.0.8-jdk-slim
WORKDIR /app
# Copy JAR từ stage builder
COPY --from=builder /app/build/libs/Technology-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV PORT=8080
# Chạy ứng dụng
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --server.port=$PORT"]
