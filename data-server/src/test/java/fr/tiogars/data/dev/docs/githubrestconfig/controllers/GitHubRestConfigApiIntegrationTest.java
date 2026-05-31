package fr.tiogars.data.dev.docs.githubrestconfig.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.tiogars.data.dev.docs.githubrestconfig.repositories.GitHubRestConfigRepository;

@SpringBootTest
class GitHubRestConfigApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GitHubRestConfigRepository gitHubRestConfigRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        gitHubRestConfigRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateListUpdateAndDeleteGitHubRestConfigByIdentifier() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String identifier = "integration-" + suffix;

        String createPayload = """
            {
              "identifier": "%s",
              "token": "github_pat_%s",
              "comment": "Creation initiale"
            }
            """.formatted(identifier, suffix);

        mockMvc.perform(post("/github-rest-config")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.identifier").value(identifier))
            .andExpect(jsonPath("$.tokenPreview").isNotEmpty())
            .andExpect(jsonPath("$.comment").value("Creation initiale"));

        mockMvc.perform(get("/github-rest-config")
                .param("page", "0")
                .param("size", "10")
                .param("q", "integration"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].identifier").value(identifier));

        String updatedIdentifier = identifier + "-v2";
        String updatePayload = """
            {
              "identifier": "%s",
              "comment": "Mise a jour sans changer le token"
            }
            """.formatted(updatedIdentifier);

        mockMvc.perform(put("/github-rest-config/{identifier}", identifier)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.identifier").value(updatedIdentifier))
            .andExpect(jsonPath("$.comment").value("Mise a jour sans changer le token"));

        mockMvc.perform(get("/github-rest-config/{identifier}", updatedIdentifier))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.identifier").value(updatedIdentifier));

        mockMvc.perform(delete("/github-rest-config/{identifier}", updatedIdentifier))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/github-rest-config/{identifier}", updatedIdentifier))
            .andExpect(status().isNotFound());
    }
}
