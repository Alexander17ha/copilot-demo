package com.microsoft.hackathon.quarkus;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/")
public class DemoResource {

    // Task 1: return the value of a key passed as query parameter.
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@QueryParam("key") String key) {
        if (key == null) {
            return "key not passed";
        }
        return "hello " + key;
    }

    // Task 2: difference in days between two dates in format dd-MM-yyyy.
    @GET
    @Path("/diffdates")
    @Produces(MediaType.TEXT_PLAIN)
    public String diffdates(@QueryParam("date1") String date1, @QueryParam("date2") String date2) {
        if (date1 == null || date2 == null) {
            return "date not passed";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            long diffMillis = Math.abs(d1.getTime() - d2.getTime());
            long diffDays = diffMillis / (24L * 60 * 60 * 1000);
            return String.valueOf(diffDays);
        } catch (Exception e) {
            return "invalid date format";
        }
    }

    // Task 3: validate a spanish phone number (+34 prefix, 9 digits starting with 6, 7 or 9).
    @GET
    @Path("/validatephone")
    @Produces(MediaType.TEXT_PLAIN)
    public String validatephone(@QueryParam("phone") String phone) {
        if (phone == null || phone.isEmpty()) {
            return "false";
        }
        return String.valueOf(phone.matches("^\\+34[679]\\d{8}$"));
    }

    // Task 4: validate a spanish DNI (8 digits and 1 letter).
    @GET
    @Path("/validatedni")
    @Produces(MediaType.TEXT_PLAIN)
    public String validatedni(@QueryParam("dni") String dni) {
        if (dni == null || dni.isEmpty()) {
            return "false";
        }
        return String.valueOf(dni.matches("^\\d{8}[A-Za-z]$"));
    }

    // Task 5: given a color name, return its hex code from colors.json, or 404 if not found.
    @GET
    @Path("/color")
    @Produces(MediaType.TEXT_PLAIN)
    public Response color(@QueryParam("color") String color) throws IOException {
        if (color == null || color.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Color not found").build();
        }
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("colors.json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(inputStream);
        for (JsonNode node : rootNode) {
            if (node.get("color").asText().equalsIgnoreCase(color)) {
                return Response.ok(node.get("code").get("hex").asText()).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Color not found").build();
    }

    // Task 6: call the Chuck Norris API and return a random joke.
    @GET
    @Path("/joke")
    @Produces(MediaType.TEXT_PLAIN)
    public String joke() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.chucknorris.io/jokes/random"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());
            return rootNode.get("value").asText();
        } catch (Exception e) {
            return "Error getting joke";
        }
    }

    // Task 7: parse a url and return protocol, host, port, path and query as JSON.
    @GET
    @Path("/parseurl")
    @Produces(MediaType.APPLICATION_JSON)
    public String parseurl(@QueryParam("url") String url) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        if (url == null || url.isEmpty()) {
            return objectMapper.createObjectNode()
                    .put("error", "url not passed")
                    .toString();
        }
        URL urlObj = new URL(url);
        return objectMapper.createObjectNode()
                .put("protocol", urlObj.getProtocol())
                .put("host", urlObj.getHost())
                .put("port", urlObj.getPort())
                .put("path", urlObj.getPath())
                .put("query", urlObj.getQuery())
                .toString();
    }

    // Task 9: count occurrences of a word in a file, return the count as JSON.
    @GET
    @Path("/countword")
    @Produces(MediaType.APPLICATION_JSON)
    public String countword(@QueryParam("path") String path, @QueryParam("word") String word) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (path == null || word == null) {
            return objectMapper.createObjectNode()
                    .put("error", "path or word not passed")
                    .toString();
        }
        try {
            String content = Files.readString(Paths.get(path));
            int count = 0;
            for (String token : content.split("\\s+")) {
                if (token.equals(word)) {
                    count++;
                }
            }
            return objectMapper.createObjectNode()
                    .put("count", count)
                    .toString();
        } catch (IOException e) {
            return objectMapper.createObjectNode()
                    .put("error", "file not found")
                    .toString();
        }
    }
}
