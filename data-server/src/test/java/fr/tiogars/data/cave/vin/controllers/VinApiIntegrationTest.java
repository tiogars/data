package fr.tiogars.data.cave.vin.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import fr.tiogars.data.cave.vin.repositories.VinCepageRepository;
import fr.tiogars.data.cave.vin.repositories.VinRepository;

@SpringBootTest
class VinApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private VinRepository vinRepository;

    @Autowired
    private VinCepageRepository vinCepageRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        vinCepageRepository.deleteAllInBatch();
        vinRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateVinWithMinimalFields() throws Exception {
        String suffix = suffix();
        String payload = """
            {
              "annee": 2022,
              "region": "Bourgogne-%s"
            }
            """.formatted(suffix);

        mockMvc.perform(post("/vin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.annee").value(2022))
            .andExpect(jsonPath("$.region").value("Bourgogne-" + suffix));
    }

    @Test
    void shouldCreateAndDeleteVin() throws Exception {
        String suffix = suffix();
        MvcResult createResult = mockMvc.perform(post("/vin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "annee": 2020,
                      "region": "Rhone-%s",
                      "commune": "Tain"
                    }
                    """.formatted(suffix)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andReturn();

        String createdId = extractId(createResult);

        mockMvc.perform(delete("/vin/{id}", createdId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/vin/{id}", createdId))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldSearchVins() throws Exception {
        String suffix = suffix();

        mockMvc.perform(post("/vin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "annee": 2021,
                      "region": "Alsace-%s"
                    }
                    """.formatted(suffix)))
            .andExpect(status().isOk());

        mockMvc.perform(post("/vin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "annee": 2022,
                      "region": "Bourgogne-%s"
                    }
                    """.formatted(suffix)))
            .andExpect(status().isOk());

        mockMvc.perform(get("/vin/search")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2))
            .andExpect(jsonPath("$.page").value(0))
            .andExpect(jsonPath("$.size").value(10))
            .andExpect(jsonPath("$.items[0].id").isNotEmpty())
            .andExpect(jsonPath("$.items[1].id").isNotEmpty());
    }

    @Test
    void shouldListVins() throws Exception {
        String suffix = suffix();

        mockMvc.perform(post("/vin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "annee": 2023,
                      "region": "Loire-%s"
                    }
                    """.formatted(suffix)))
            .andExpect(status().isOk());

        mockMvc.perform(get("/vin/list"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].id").isNotEmpty())
            .andExpect(jsonPath("$.items[0].region").value("Loire-" + suffix));
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
