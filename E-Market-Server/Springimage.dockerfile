FROM openjdk:17

ENV SPRING_PROFILES_ACTIVE=prod

COPY ./effortless-market-0.1.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]