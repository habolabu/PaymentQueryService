FROM gradle:jdk17-alpine
WORKDIR /payment-query-service

COPY src /payment-query-service/src/
COPY build.gradle .
COPY lombok.config .

RUN gradle clean build -x test

ENTRYPOINT ["java", "-jar", "./build/libs/payment-query-service-1.0.0.jar"]