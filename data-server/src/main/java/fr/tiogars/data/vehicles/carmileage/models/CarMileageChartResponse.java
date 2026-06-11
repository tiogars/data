package fr.tiogars.data.vehicles.carmileage.models;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class CarMileageChartResponse {

    @Schema(description = "L'identifiant de la voiture.", example = "123e4567-e89b-12d3-a456-426614174000")
    private String carId;

    @Schema(description = "Le nom de la voiture.", example = "Clio 3")
    private String carName;

    @Schema(description = "Les points du graphique ordonnes chronologiquement.")
    private List<CarMileageChartPoint> points;

    public CarMileageChartResponse(String carId, String carName, List<CarMileageChartPoint> points) {
        this.carId = carId;
        this.carName = carName;
        this.points = points;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public List<CarMileageChartPoint> getPoints() {
        return points;
    }

    public void setPoints(List<CarMileageChartPoint> points) {
        this.points = points;
    }
}
