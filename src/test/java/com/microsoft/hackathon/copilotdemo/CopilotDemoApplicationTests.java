package com.microsoft.hackathon.copilotdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class CopilotDemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void hello() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello?key=Copilot"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("hello Copilot"));
    }

    @Test
    void helloNoKey() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("key not passed"));
    }

    @Test
    void diffdates() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/diffdates?date1=01-01-2020&date2=05-01-2020"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("difference in days: 4"));
    }

    @Test
    void diffdatesNoDates() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/diffdates"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("date not passed"));
    }

    @Test
    void validatephone() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/validatephone?phone=+34666666666"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
        mockMvc.perform(MockMvcRequestBuilders.get("/validatephone?phone=+34866666666"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    @Test
    void validatephoneNoPhone() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/validatephone"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    @Test
    void validatedni() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/validatedni?dni=12345678C"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    void validatedniNoDni() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/validatedni"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    @Test
    void colorNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/color/notacolor"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void joke() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/joke"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.value").exists());
                                                
    }

    @Test
    void parseUrl() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/parseurl?url=https://learn.microsoft.com/en-us/azure/aks/concepts-clusters-workloads?source=recommendations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.protocol").value("https"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.host").value("learn.microsoft.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/en-us/azure/aks/concepts-clusters-workloads"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.query").value("source=recommendations"));
    }

    @Test
    void parseUrlNoUrl() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/parseurl"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("url not passed"));
    }

    @Test
    void countWord() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/countword?path=src/test/resources/test.txt&word=hello"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(3));
    }
}
