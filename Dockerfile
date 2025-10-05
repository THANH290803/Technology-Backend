# Sử dụng OpenJDK 21
FROM openjdk:21-jdk-slim

# Thư mục làm việc
WORKDIR /app

# Copy file JAR
COPY build/libs/Technology-0.0.1-SNAPSHOT.jar app.jar

# Mở port 8080
EXPOSE 8080

# Biến môi trường PORT do Render cung cấp
ENV PORT=8080

# Chạy ứng dụng
ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar --server.port=$PORT"]
