# Build a java application image based on java 17 and run it on port 8080
FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]