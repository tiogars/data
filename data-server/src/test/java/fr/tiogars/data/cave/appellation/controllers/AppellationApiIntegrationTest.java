package fr.tiogars.data.cave.appellation.controllers;

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

import fr.tiogars.data.cave.appellation.repositories.AppellationRepository;

@SpringBootTest
class AppellationApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AppellationRepository appellationRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        appellationRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateUpdateAndDeleteAppellation() throws Exception {
        String suffix = suffix();
        String createPayload = """
            {
              "name": "Appellation-%s"
            }
            """.formatted(suffix);

        MvcResult createResult = mockMvc.perform(post("/appellation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("Appellation-" + suffix))
            .andReturn();

        String createdId = extractId(createResult);

        mockMvc.perform(get("/appellation/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].id").value(createdId))
            .andExpect(jsonPath("$.items[0].name").value("Appellation-" + suffix));

        String updatePayload = """
            {
              "id": "%s",
              "name": "Appellation-%s-updated"
            }
            """.formatted(createdId, suffix);

        mockMvc.perform(put("/appellation/{id}", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.name").value("Appellation-" + suffix + "-updated"));

        mockMvc.perform(get("/appellation/{id}", createdId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.name").value("Appellation-" + suffix + "-updated"));

        mockMvc.perform(delete("/appellation/{id}", createdId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/appellation/{id}", createdId))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldSearchAppellations() throws Exception {
        String suffix = suffix();

        mockMvc.perform(post("/appellation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Champagne-%s"
                    }
                    """.formatted(suffix)))
            .andExpect(status().isOk());

        mockMvc.perform(post("/appellation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Bordeaux-%s"
                    }
                    """.formatted(suffix)))
            .andExpect(status().isOk());

        mockMvc.perform(get("/appellation/search")
                .param("page", "0")
                .param("size", "10")
                .param("q", "Champagne-" + suffix))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.items[0].name").value("Champagne-" + suffix));
    }

    @Test
    void shouldImportAndExportAppellations() throws Exception {
        String suffix = suffix();
        String importPayload = """
            {
              "text": "Alsace-%s\\nBourgogne-%s\\nLoire-%s"
            }
            """.formatted(suffix, suffix, suffix);

        mockMvc.perform(post("/appellation/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(importPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.addedCount").value(3))
            .andExpect(jsonPath("$.alreadyExistsCount").value(0))
            .andExpect(jsonPath("$.invalidCount").value(0));

        mockMvc.perform(get("/appellation/export"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(3))
            .andExpect(jsonPath("$.items[0].name").isNotEmpty())
            .andExpect(jsonPath("$.items[1].name").isNotEmpty())
            .andExpect(jsonPath("$.items[2].name").isNotEmpty());
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
