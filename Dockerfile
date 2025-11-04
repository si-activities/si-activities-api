FROM ubuntu:latest AS build

RUN apt-get update && apt-get install openjdk-17-jdk -y \
	&& apt-get clean

COPY . .

RUN apt-get install maven -y && apt-get clean
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jdk

EXPOSE 8080

COPY --from=build /target/server-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]
