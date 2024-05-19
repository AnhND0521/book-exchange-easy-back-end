FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/book-exchange-easy-0.0.1-SNAPSHOT.jar /app/book-exchange-easy.jar

CMD ["java", "-jar", "book-exchange-easy.jar"]
