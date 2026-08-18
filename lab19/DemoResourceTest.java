package com.microsoft.hackathon.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class DemoResourceTest {

    // Task 1
    @Test
    void hello() {
        given()
            .queryParam("key", "world")
            .when().get("/hello")
            .then().statusCode(200).body(is("hello world"));
    }

    @Test
    void helloNoKey() {
        given()
            .when().get("/hello")
            .then().statusCode(200).body(is("key not passed"));
    }

    // Task 2
    @Test
    void diffdates() {
        given()
            .queryParam("date1", "01-01-2021")
            .queryParam("date2", "01-02-2021")
            .when().get("/diffdates")
            .then().statusCode(200).body(is("31"));
    }

    @Test
    void diffdatesNoDate() {
        given()
            .queryParam("date1", "01-01-2021")
            .when().get("/diffdates")
            .then().statusCode(200).body(is("date not passed"));
    }

    // Task 3
    @Test
    void validatephoneValid() {
        given()
            .queryParam("phone", "+34666666666")
            .when().get("/validatephone")
            .then().statusCode(200).body(is("true"));
    }

    @Test
    void validatephoneInvalid() {
        given()
            .queryParam("phone", "+34866666666")
            .when().get("/validatephone")
            .then().statusCode(200).body(is("false"));
    }

    // Task 4
    @Test
    void validatedniValid() {
        given()
            .queryParam("dni", "12345678A")
            .when().get("/validatedni")
            .then().statusCode(200).body(is("true"));
    }

    @Test
    void validatedniInvalid() {
        given()
            .queryParam("dni", "1234567A")
            .when().get("/validatedni")
            .then().statusCode(200).body(is("false"));
    }

    // Task 5
    @Test
    void colorFound() {
        given()
            .queryParam("color", "red")
            .when().get("/color")
            .then().statusCode(200).body(is("#FF0000"));
    }

    @Test
    void colorNotFound() {
        given()
            .queryParam("color", "notacolor")
            .when().get("/color")
            .then().statusCode(404);
    }

    // Task 6
    @Test
    void joke() {
        given()
            .when().get("/joke")
            .then().statusCode(200);
    }

    // Task 7
    @Test
    void parseUrl() {
        given()
            .queryParam("url", "https://www.google.com/search?q=quarkus")
            .when().get("/parseurl")
            .then().statusCode(200)
            .body("protocol", is("https"))
            .body("host", is("www.google.com"));
    }

    @Test
    void parseUrlNoUrl() {
        given()
            .when().get("/parseurl")
            .then().statusCode(200).body("error", is("url not passed"));
    }

    // Task 9
    @Test
    void countWord() throws Exception {
        File tmp = File.createTempFile("wordtest", ".txt");
        Files.writeString(tmp.toPath(), "hello world hello copilot hello");
        given()
            .queryParam("path", tmp.getAbsolutePath())
            .queryParam("word", "hello")
            .when().get("/countword")
            .then().statusCode(200).body("count", is(3));
        tmp.delete();
    }
}
