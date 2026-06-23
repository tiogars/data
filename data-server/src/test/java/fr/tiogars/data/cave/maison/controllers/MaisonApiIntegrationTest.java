package fr.tiogars.data.cave.maison.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.tiogars.data.cave.maison.repositories.MaisonRepository;

@SpringBootTest
class MaisonApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MaisonRepository maisonRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        maisonRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateUpdateAndDeleteMaison() throws Exception {
        String suffix = suffix();
        String createPayload = """
            {
              "name": "Maison-%s",
              "website": "https://maison-%s.example"
            }
            """.formatted(suffix, suffix);

        MvcResult createResult = mockMvc.perform(post("/maison")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("Maison-" + suffix))
            .andExpect(jsonPath("$.website").value("https://maison-" + suffix + ".example"))
            .andReturn();

        String createdId = extractId(createResult);

        mockMvc.perform(get("/maison/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].id").value(createdId))
            .andExpect(jsonPath("$.items[0].website").value("https://maison-" + suffix + ".example"));

        String updatePayload = """
            {
              "id": "%s",
              "name": "Maison-%s-updated",
              "website": "https://maison-%s-updated.example"
            }
            """.formatted(createdId, suffix, suffix);

        mockMvc.perform(put("/maison/{id}", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.name").value("Maison-" + suffix + "-updated"))
            .andExpect(jsonPath("$.website").value("https://maison-" + suffix + "-updated.example"));

        mockMvc.perform(get("/maison/{id}", createdId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.name").value("Maison-" + suffix + "-updated"));

        mockMvc.perform(delete("/maison/{id}", createdId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/maison/{id}", createdId))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldSearchMaisons() throws Exception {
        String suffix = suffix();

        mockMvc.perform(post("/maison")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Maison-%s",
                      "website": "https://champagne-%s.example"
                    }
                    """.formatted(suffix, suffix)))
            .andExpect(status().isOk());

        mockMvc.perform(post("/maison")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Maison-Autre-%s",
                      "website": "https://autre-%s.example"
                    }
                    """.formatted(suffix, suffix)))
            .andExpect(status().isOk());

        mockMvc.perform(get("/maison/search")
                .param("page", "0")
                .param("size", "10")
                .param("q", "champagne-" + suffix))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.items[0].name").value("Maison-" + suffix));
    }

    private static String suffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private static String extractId(MvcResult result) throws Exception {
        String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Pattern pattern = Pattern.compile("\"id\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(response);
        if (!matcher.find()) {
            throw new IllegalStateException("Impossible de trouver l'id dans la reponse: " + response);
        }
        return matcher.group(1);
    }
}
