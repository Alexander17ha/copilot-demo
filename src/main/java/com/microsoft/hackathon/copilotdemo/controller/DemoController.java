package com.microsoft.hackathon.copilotdemo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class DemoController {

    @GetMapping("/hello")
    public String hello(@RequestParam(name = "key", required = false) String key) {
        if (key == null) {
            return "key not passed";
        }
        return "hello " + key;
    }

    @GetMapping("/diffdates")
    public String diffdates(@RequestParam(name = "date1", required = false) String date1,
            @RequestParam(name = "date2", required = false) String date2) throws ParseException {
        if (date1 == null || date2 == null) {
            return "date not passed";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date1Obj = sdf.parse(date1);
        Date date2Obj = sdf.parse(date2);
        long diffInMillies = Math.abs(date2Obj.getTime() - date1Obj.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return "difference in days: " + diff;
    }

    @GetMapping("/validatephone")
    public boolean validatephone(@RequestParam(name = "phone", required = false) String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        String regex = "^\\+34[679]\\d{8}$";
        return phone.matches(regex);
    }

    @GetMapping("/validatedni")
    public boolean validatedni(@RequestParam(name = "dni", required = false) String dni) {
        if (dni == null || dni.isEmpty()) {
            return false;
        }
        String regex = "^\\d{8}[A-Z]$";
        return dni.matches(regex);
    }

    @GetMapping("/color/{name}")
    public ResponseEntity<String> color(@PathVariable("name") String name) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("colors.json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(inputStream);
        for (JsonNode color : rootNode) {
            if (color.get("color").asText().equals(name)) {
                return new ResponseEntity<String>(color.get("code").get("hex").asText(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<String>("Color not found", HttpStatus.NOT_FOUND);
    }

   @GetMapping(value = "/joke", produces = "application/json")
   public String getJoke() {
        RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.chucknorris.io/jokes/random";
                org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
                    headers.set("Accept", "application/json");
                        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
                            ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);
                                return response.getBody();
   
   }

    @GetMapping("/parseurl")
    public String parseUrl(@RequestParam(value = "url", required = false) String url) {
        if (url == null || url.isEmpty()) {
            return "url not passed";
        }
        java.net.URL parsedUrl;
        try {
            parsedUrl = new java.net.URL(url);
        } catch (java.net.MalformedURLException e) {
            return "invalid url";
        }
        String protocol = parsedUrl.getProtocol();
        String host = parsedUrl.getHost();
        int port = parsedUrl.getPort();
        String path = parsedUrl.getPath();
        String query = parsedUrl.getQuery();
        return "{ \"protocol\": \"" + protocol + "\", \"host\": \"" + host + "\", \"port\": " + port + ", \"path\": \"" + path + "\", \"query\": \"" + query + "\" }";
    }

    @GetMapping("/countword")
    public String countWord(@RequestParam(value = "path", required = false) String path,
            @RequestParam(value = "word", required = false) String word) throws IOException {
        if (path == null || path.isEmpty() || word == null || word.isEmpty()) {
            return "path or word not passed";
        }
        java.io.File file = new java.io.File(path);
        if (!file.exists()) {
            return "file not found";
        }
        String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        int count = content.split(word, -1).length - 1;
        return "{ \"count\": " + count + " }";
    }
}
