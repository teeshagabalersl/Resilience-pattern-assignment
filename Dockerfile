FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY src ./src

RUN mkdir -p classes && \
    javac -d classes $(find src -name "*.java")

WORKDIR /app/classes