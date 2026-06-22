package fr.tiogars.data.softwares.winget.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Liste des applications Windows installables par Winget avec le nombre total.")
public class WingetListResponse extends GenericListResponse<Winget> {

    public WingetListResponse(List<Winget> items) {
        super(items);
    }

    public WingetListResponse(List<Winget> items, int count) {
        super(items);
        setCount(count);
    }
}
