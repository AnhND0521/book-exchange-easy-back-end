FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

COPY target/book-exchange-easy-0.0.1-SNAPSHOT.jar /app/book-exchange-easy.jar

ENTRYPOINT ["java", "-jar", "book-exchange-easy.jar"]
