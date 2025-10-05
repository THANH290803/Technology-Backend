# Sử dụng OpenJDK 21
FROM openjdk:21-jdk-slim

# Thư mục làm việc trong container
WORKDIR /app

# Copy file jar build từ Gradle
COPY build/libs/*.jar app.jar

# Mở port 8080
EXPOSE 8080

# Biến môi trường PORT do Render cung cấp
ENV PORT=8080

# Chạy ứng dụng và lấy port từ biến PORT
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --server.port=$PORT"]
