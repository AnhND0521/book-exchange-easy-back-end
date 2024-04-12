FROM openjdk:17-alpine

RUN apk add --no-cache maven

WORKDIR /app

COPY . /app

RUN mvn clean package -DskipTests

WORKDIR /app/target

CMD ["java", "-jar", "book-exchange-easy-0.0.1-SNAPSHOT.jar"]
