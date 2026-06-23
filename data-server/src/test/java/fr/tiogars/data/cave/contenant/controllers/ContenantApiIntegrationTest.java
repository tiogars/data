package fr.tiogars.data.cave.contenant.controllers;

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

import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;

@SpringBootTest
class ContenantApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ContenantRepository contenantRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        contenantRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateUpdateAndDeleteContenant() throws Exception {
        String suffix = suffix();
        String createPayload = """
            {
              "name": "Bouteille-%s",
              "volumeCl": 75
            }
            """.formatted(suffix);

        MvcResult createResult = mockMvc.perform(post("/contenant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("Bouteille-" + suffix))
            .andExpect(jsonPath("$.volumeCl").value(75))
            .andReturn();

        String createdId = extractId(createResult);

        mockMvc.perform(get("/contenant/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].id").value(createdId))
            .andExpect(jsonPath("$.items[0].volumeCl").value(75));

        String updatePayload = """
            {
              "id": "%s",
              "name": "Magnum-%s",
              "volumeCl": 150
            }
            """.formatted(createdId, suffix);

        mockMvc.perform(put("/contenant/{id}", createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.name").value("Magnum-" + suffix))
            .andExpect(jsonPath("$.volumeCl").value(150));

        mockMvc.perform(get("/contenant/{id}", createdId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(createdId))
            .andExpect(jsonPath("$.name").value("Magnum-" + suffix))
            .andExpect(jsonPath("$.volumeCl").value(150));

        mockMvc.perform(delete("/contenant/{id}", createdId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/contenant/{id}", createdId))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldSearchContenants() throws Exception {
        String suffix = suffix();

        mockMvc.perform(post("/contenant")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Bouteille-%s",
                      "volumeCl": 75
                    }
                    """.formatted(suffix)))
            .andExpect(status().isOk());

        mockMvc.perform(post("/contenant")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "Jeroboam-%s",
                      "volumeCl": 300
                    }
                    """.formatted(suffix)))
            .andExpect(status().isOk());

        mockMvc.perform(get("/contenant/search")
                .param("page", "0")
                .param("size", "10")
                .param("q", "Jeroboam-" + suffix))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.items[0].name").value("Jeroboam-" + suffix));
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
