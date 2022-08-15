#
# Build stage
#
FROM maven:3.8.4-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
# build with docker profile, could also be used with -Pprod for prod profile and -Pdev for dev profile
RUN mvn -f /home/app/pom.xml clean package -Pdocker

#
# Package stage
#
FROM openjdk:17-slim
COPY --from=build /home/app/target/gafalag-api-1.0-SNAPSHOT.jar ./gafalag-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","./gafalag-api.jar"]

# docker build -t gafalag-api:latest .

# docker run -d \
#              --restart always \
#              -p 0.0.0.0:80:8080 \
#              --name gafalag-api \
#              gafalag-api:latest \
#              --gafalag.db.url=jdbc:postgresql://localhost:5433/gafalag_db \
#              --gafalag.db.username=docker \
#              --gafalag.db.password=docker