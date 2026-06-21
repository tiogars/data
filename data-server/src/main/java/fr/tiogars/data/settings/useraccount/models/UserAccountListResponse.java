package fr.tiogars.data.settings.useraccount.models;

import java.util.List;

import fr.tiogars.data.common.models.GenericListResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Liste des comptes utilisateurs avec le nombre total.")
public class UserAccountListResponse extends GenericListResponse<UserAccount> {

    public UserAccountListResponse(List<UserAccount> items) {
        super(items);
    }

    public UserAccountListResponse(List<UserAccount> items, int count) {
        super(items);
        setCount(count);
    }
}
