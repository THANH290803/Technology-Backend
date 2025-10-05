# Stage 1: Build Gradle
FROM gradle:8.3.3-jdk21 AS builder
WORKDIR /app
COPY . .
# Build JAR, bỏ qua test để chắc chắn build thành công
RUN gradle clean build -x test

# Stage 2: Chạy ứng dụng
FROM openjdk:21-jdk-slim
WORKDIR /app
# Copy JAR từ stage 1
COPY --from=builder /app/build/libs/Technology-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV PORT=8080
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --server.port=$PORT"]
