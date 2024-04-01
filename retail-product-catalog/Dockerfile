
FROM openjdk:21

RUN mkdir  -p /app

VOLUME /app

WORKDIR /app

ARG JAR_FILE=/target/retail-product-catalog-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} retail-product-catalog.jar

EXPOSE 8090

ENTRYPOINT ["java","-jar","retail-product-catalog.jar"]