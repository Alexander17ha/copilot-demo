# Docker

## Build the jar
mvn clean package

## Build the image
docker build -t my-application .

## Run the container
docker run -d -p 8080:8080 my-application

## Test
curl.exe "http://localhost:8080/hello?key=world"

## Stop
docker ps
docker stop <container_id>