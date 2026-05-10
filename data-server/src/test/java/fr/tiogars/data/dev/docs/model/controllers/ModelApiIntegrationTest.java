package fr.tiogars.data.dev.docs.model.controllers;

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

import fr.tiogars.data.dev.docs.model.repositories.ModelRepository;

@SpringBootTest
class ModelApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ModelRepository modelRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        modelRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateUpdateAndDeleteModel() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String createPayload = """
            {
              "name": "Modele-%s",
              "description": "Description initiale"
            }
            """.formatted(suffix);

        MvcResult createResult = mockMvc.perform(post("/model")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("Modele-" + suffix))
            .andExpect(jsonPath("$.description").value("Description initiale"))
            .andReturn();

        String createdId = extractId(createResult);

        mockMvc.perform(get("/model"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].id").value(createdId))
            .andExpect(jsonPath("$.items[0].name").value("Modele-" + suffix));

        String updatePayload = """
            {
              "id": "%s",
              "name": "Modele-%s-updated",
              "description": "Description mise a jour"
            }
            """.formatted(createdId, suffix);

        mockMvc.perform(put("/model/{id}", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.name").value("Modele-" + suffix + "-updated"))
            .andExpect(jsonPath("$.description").value("Description mise a jour"));

        mockMvc.perform(get("/model/{id}", createdId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.name").value("Modele-" + suffix + "-updated"));

        mockMvc.perform(delete("/model/{id}", createdId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/model/{id}", createdId))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldImportExportAndPrintModels() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String importPayload = """
            {
              "items": [
                {
                  "name": "Alpha-%s",
                  "description": "A"
                },
                {
                  "name": "Beta-%s",
                  "description": "B"
                },
                {
                  "name": "Alpha-%s",
                  "description": "A duplicate"
                }
              ]
            }
            """.formatted(suffix, suffix, suffix);

        mockMvc.perform(post("/model/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(importPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.importedCount").value(2))
            .andExpect(jsonPath("$.skippedCount").value(1))
            .andExpect(jsonPath("$.duplicateNames[0]").value("Alpha-" + suffix));

        mockMvc.perform(get("/model/export"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2));

        mockMvc.perform(get("/model/print").param("mode", "all"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2))
            .andExpect(jsonPath("$.total").value(2))
            .andExpect(jsonPath("$.generatedAt").isNotEmpty());

        mockMvc.perform(get("/model/print")
                .param("mode", "filtered")
                .param("name", "Alpha-" + suffix))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.total").value(2));
    }

    @Test
    void shouldDeleteAllModels() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        String firstPayload = """
            {
              "name": "First-%s",
              "description": "First"
            }
            """.formatted(suffix);

        String secondPayload = """
            {
              "name": "Second-%s",
              "description": "Second"
            }
            """.formatted(suffix);

        mockMvc.perform(post("/model")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstPayload))
            .andExpect(status().isOk());

        mockMvc.perform(post("/model")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondPayload))
            .andExpect(status().isOk());

        mockMvc.perform(get("/model"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2));

        mockMvc.perform(delete("/model"))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/model"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(0));
    }

    private static String extractId(MvcResult result) throws Exception {
        String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Pattern pattern = Pattern.compile("\\\"id\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");
        Matcher matcher = pattern.matcher(response);
        if (!matcher.find()) {
            throw new IllegalStateException("Impossible de trouver l'id dans la reponse: " + response);
        }
        return matcher.group(1);
    }
}
