package fr.tiogars.data.products.gtin.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.tiogars.data.products.gtin.repositories.GtinRepository;

@SpringBootTest
class GtinImportExportCsvIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GtinRepository gtinRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        gtinRepository.deleteAllInBatch();
    }

    @Test
    void shouldImportAndExportGtinsAsCsv() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String codeA = "000000" + suffix;
        String codeB = "111111" + suffix;

        String csvPayload = """
            code,description
            %s,Produit A
            %s,\"Produit, B\"
            %s,Doublon
            """.formatted(codeA, codeB, codeA);

        mockMvc.perform(post("/gtin/import/csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .content(csvPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.importedCount").value(2))
            .andExpect(jsonPath("$.skippedCount").value(1))
            .andExpect(jsonPath("$.duplicateCodes[0]").value(codeA));

        MvcResult exportResult = mockMvc.perform(get("/gtin/export/csv"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"gtin-export.csv\""))
            .andReturn();

        String exportedCsv = exportResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(exportedCsv).contains("code,description");
        assertThat(exportedCsv).contains(codeA + ",Produit A");
        assertThat(exportedCsv).contains(codeB + ",\"Produit, B\"");
    }

    @Test
    void shouldImportSemicolonCsvUsingHeaderNames() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String codeA = "222222" + suffix;
        String codeB = "333333" + suffix;

        String csvPayload = """
            description;code
            Produit A;%s
            Produit B;%s
            """.formatted(codeA, codeB);

        mockMvc.perform(post("/gtin/import/csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .content(csvPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.importedCount").value(2));

        mockMvc.perform(get("/gtin/export"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2))
            .andExpect(jsonPath("$.items[0].code").value(codeA))
            .andExpect(jsonPath("$.items[0].description").value("Produit A"))
            .andExpect(jsonPath("$.items[1].code").value(codeB))
            .andExpect(jsonPath("$.items[1].description").value("Produit B"));
    }
}
