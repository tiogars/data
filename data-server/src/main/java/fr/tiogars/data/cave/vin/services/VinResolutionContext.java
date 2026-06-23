package fr.tiogars.data.cave.vin.services;

import java.util.Map;

record VinResolutionContext(
    Map<String, String> appellationNames,
    Map<String, String> couleurNames,
    Map<String, String> typeVinNames,
    Map<String, String> maisonNames,
    Map<String, String> vinNomNames,
    Map<String, String> contenantNames,
    Map<String, String> cepageNames,
    Map<String, String> circonstanceNames,
    Map<String, String> tagNames
) {

    static VinResolutionContext empty() {
        return new VinResolutionContext(
            Map.of(),
            Map.of(),
            Map.of(),
            Map.of(),
            Map.of(),
            Map.of(),
            Map.of(),
            Map.of(),
            Map.of()
        );
    }
}
