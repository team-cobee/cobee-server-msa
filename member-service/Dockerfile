FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY gradlew gradle.properties settings.gradle build.gradle ./
COPY gradle ./gradle
COPY src ./src
RUN chmod +x ./gradlew
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew -q clean bootJar


FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75"

EXPOSE 8081

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]