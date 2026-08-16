package fr.tiogars.data.cave.vin.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import fr.tiogars.data.cave.vin.entities.VinEntity;

class VinModelMapperTest {

    @Test
    void shouldKeepOptionalLookupNamesNullWhenForeignKeysAreMissing() {
        VinEntity entity = new VinEntity();
        entity.setId("vin-1");
        entity.setAnnee(2022);
        entity.setRegion("Bourgogne");

        var model = VinModelMapper.toModel(entity, null, null, null, VinResolutionContext.empty());

        assertThat(model.getId()).isEqualTo("vin-1");
        assertThat(model.getAnnee()).isEqualTo(2022);
        assertThat(model.getRegion()).isEqualTo("Bourgogne");
        assertThat(model.getAppellationName()).isNull();
        assertThat(model.getCouleurName()).isNull();
        assertThat(model.getTypeVinName()).isNull();
        assertThat(model.getMaisonName()).isNull();
        assertThat(model.getVinNomName()).isNull();
        assertThat(model.getContenantName()).isNull();
        assertThat(model.getCepages()).isEmpty();
        assertThat(model.getCirconstances()).isEmpty();
        assertThat(model.getTags()).isEmpty();
    }
}
