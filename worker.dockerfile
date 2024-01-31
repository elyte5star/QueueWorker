FROM --platform=linux/amd64 eclipse-temurin:17
LABEL maintainer="Ogaga Uti <checkuti@gmail.com>"
LABEL version="0.0.1"
LABEL description="Worker for Spring Boot application for E-commerce"
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


