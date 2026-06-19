package fr.tiogars.data.dev.docs.car.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
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

import fr.tiogars.data.vehicles.car.repositories.CarRepository;
import fr.tiogars.data.vehicles.carmileage.repositories.CarMileageRepository;

@SpringBootTest
class CarAndMileageApiIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarMileageRepository carMileageRepository;

    @BeforeEach
    void cleanData() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        carMileageRepository.deleteAllInBatch();
        carRepository.deleteAllInBatch();
    }

    @Test
    void shouldCreateSearchChartAndDeleteCarMileage() throws Exception {
        String carPayload = """
            {
              "name": "Clio test",
              "vehicleRegistrationPlate": "AA-123-AA",
              "description": "Voiture de test"
            }
            """;

        MvcResult createCarResult = mockMvc.perform(post("/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content(carPayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value("Clio test"))
            .andReturn();

        String carId = extractId(createCarResult);

        String firstMileagePayload = """
            {
              "carId": "%s",
              "readingAt": "2026-06-01T08:00:00",
              "odometerKm": 100000,
              "fuelVolumeLiters": 35.5,
              "fullTank": true
            }
            """.formatted(carId);

        String secondMileagePayload = """
            {
              "carId": "%s",
              "readingAt": "2026-06-10T18:30:00",
              "odometerKm": 100420,
              "fullTank": false
            }
            """.formatted(carId);

        mockMvc.perform(post("/car-mileage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(firstMileagePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.carId").value(carId))
            .andExpect(jsonPath("$.odometerKm").value(100000))
            .andExpect(jsonPath("$.fullTank").value(true));

        MvcResult secondMileageResult = mockMvc.perform(post("/car-mileage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(secondMileagePayload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.carId").value(carId))
            .andExpect(jsonPath("$.odometerKm").value(100420))
            .andReturn();

        String mileageId = extractId(secondMileageResult);

        mockMvc.perform(get("/car/search").param("q", "clio"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.items[0].id").value(carId));

        mockMvc.perform(get("/car-mileage/search")
                .param("carId", carId)
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2))
            .andExpect(jsonPath("$.items[0].odometerKm").value(100420))
            .andExpect(jsonPath("$.items[1].odometerKm").value(100000));

        mockMvc.perform(get("/car-mileage/chart").param("carId", carId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.carId").value(carId))
            .andExpect(jsonPath("$.points.length()").value(2))
            .andExpect(jsonPath("$.points[0].odometerKm").value(100000))
            .andExpect(jsonPath("$.points[1].odometerKm").value(100420));

        mockMvc.perform(get("/car-mileage/export"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.count").value(2))
            .andExpect(jsonPath("$.items[0].carName").value("Clio test"))
            .andExpect(jsonPath("$.items[0].vehicleRegistrationPlate").value("AA-123-AA"));

        MvcResult exportCsvResult = mockMvc.perform(get("/car-mileage/export/csv"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"car-mileage-export.csv\""))
            .andReturn();

        String exportedCsv = exportCsvResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(exportedCsv).contains("carName,vehicleRegistrationPlate,readingAt,odometerKm,fuelVolumeLiters,fullTank");
        assertThat(exportedCsv).contains("Clio test,AA-123-AA,2026-06-01T08:00,100000,35.5,true");
        assertThat(exportedCsv).doesNotContain(carId);

        mockMvc.perform(delete("/car-mileage/{id}", mileageId))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/car-mileage/{id}", mileageId))
            .andExpect(status().isNotFound());
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
