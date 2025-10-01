FROM eclipse-temurin:21-jdk as builder

WORKDIR /app

COPY . .

RUN ./gradlew clean build --no-daemon

FROM eclipse-temurin:21-jre as runtime

WORKDIR /server

COPY --from=builder /app/out/ProfessionsPlugin.jar /server/plugins/ProfessionsPlugin.jar
COPY purpur-1.21.5.jar /server/server.jar

EXPOSE 25565

CMD ["java", "-Xmx2G", "-jar", "server.jar", "nogui"]
