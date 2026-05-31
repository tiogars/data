package fr.tiogars.data.dev.docs.continent.controllers;

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

import fr.tiogars.data.locations.continent.repositories.ContinentRepository;

/**
 * Tests d'intégration pour l'API Continent.
 */
@SpringBootTest
class ContinentApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ContinentRepository continentRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        continentRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateUpdateAndDeleteContinent() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String createPayload = """
            {
              "code": "eu-%s",
              "name": "Europe-%s"
            }
            """.formatted(suffix, suffix);

        MvcResult createResult = mockMvc.perform(post("/continent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.code").value("eu-" + suffix))
            .andExpect(jsonPath("$.name").value("Europe-" + suffix))
            .andReturn();

        String createdId = extractId(createResult);

        mockMvc.perform(get("/continent"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].id").value(createdId))
            .andExpect(jsonPath("$.items[0].code").value("eu-" + suffix));

        String updatePayload = """
            {
              "id": "%s",
              "code": "eu-%s-updated",
              "name": "Europe-%s-updated"
            }
            """.formatted(createdId, suffix, suffix);

        mockMvc.perform(put("/continent/{id}", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.code").value("eu-" + suffix + "-updated"))
            .andExpect(jsonPath("$.name").value("Europe-" + suffix + "-updated"));

        mockMvc.perform(get("/continent/{id}", createdId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.code").value("eu-" + suffix + "-updated"));

        mockMvc.perform(delete("/continent/{id}", createdId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/continent/{id}", createdId))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnEmptyListWhenNoContinents() throws Exception {
        mockMvc.perform(get("/continent"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(0))
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items.length()").value(0));
    }

    @Test
    void shouldThrowErrorWhenCreatingDuplicateCode() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String createPayload = """
            {
              "code": "eu-%s",
              "name": "Europe-%s"
            }
            """.formatted(suffix, suffix);

        mockMvc.perform(post("/continent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk());

        String secondPayload = """
            {
              "code": "eu-%s",
              "name": "Europe-Duplicate-%s"
            }
            """.formatted(suffix, suffix);

        mockMvc.perform(post("/continent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondPayload))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldThrowErrorWhenCreatingDuplicateName() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String createPayload = """
            {
              "code": "eu-%s",
              "name": "Europe-%s"
            }
            """.formatted(suffix, suffix);

        mockMvc.perform(post("/continent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk());

        String secondPayload = """
            {
              "code": "as-%s",
              "name": "Europe-%s"
            }
            """.formatted(suffix, suffix);

        mockMvc.perform(post("/continent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondPayload))
            .andExpect(status().isBadRequest());
    }

    private String extractId(MvcResult result) throws Exception {
        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Pattern pattern = Pattern.compile("\"id\":\"([a-f0-9\\-]+)\"");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new AssertionError("ID not found in response: " + content);
    }
}
