FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/kafka-intro-1.0.0.jar /app/kafka-intro-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/kafka-intro-1.0.0.jar"]
