FROM jelastic/maven:3.9.5-openjdk-21 AS builder
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN sh -c "mvn install"

FROM openjdk:21-slim
WORKDIR /app
COPY --from=builder /app/target/sql-0.0.1-SNAPSHOT.jar sql-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "sql-0.0.1-SNAPSHOT.jar"]