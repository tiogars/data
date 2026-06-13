package fr.tiogars.data.sync.services;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import fr.tiogars.data.products.gtin.entities.GtinEntity;
import fr.tiogars.data.products.gtin.models.Gtin;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;
import fr.tiogars.data.softwares.android.entities.AndroidEntity;
import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.repositories.AndroidRepository;
import fr.tiogars.data.sync.models.SyncChangesResponse;
import fr.tiogars.data.vehicles.car.entities.CarEntity;
import fr.tiogars.data.vehicles.car.models.Car;
import fr.tiogars.data.vehicles.car.repositories.CarRepository;
import fr.tiogars.data.vehicles.carmileage.entities.CarMileageEntity;
import fr.tiogars.data.vehicles.carmileage.models.CarMileage;
import fr.tiogars.data.vehicles.carmileage.repositories.CarMileageRepository;

@Service
public class SyncChangesService {

    private static final int DEFAULT_SIZE = 100;
    private static final int MAX_SIZE = 500;
    private final GtinRepository gtinRepository;
    private final CarRepository carRepository;
    private final AndroidRepository androidRepository;
    private final CarMileageRepository carMileageRepository;
    private final SyncCursorCodec cursorCodec;
    private final SyncDeletionEventService syncDeletionEventService;

    public SyncChangesService(
        GtinRepository gtinRepository,
        CarRepository carRepository,
        AndroidRepository androidRepository,
        CarMileageRepository carMileageRepository,
        SyncCursorCodec cursorCodec,
        SyncDeletionEventService syncDeletionEventService
    ) {
        this.gtinRepository = gtinRepository;
        this.carRepository = carRepository;
        this.androidRepository = androidRepository;
        this.carMileageRepository = carMileageRepository;
        this.cursorCodec = cursorCodec;
        this.syncDeletionEventService = syncDeletionEventService;
    }

    public SyncChangesResponse<Gtin> getGtinChanges(String cursor, String updatedAfter, Integer requestedSize) {
        List<OrderedChange<Gtin>> ordered = gtinRepository.findAll().stream()
            .map(this::toGtin)
            .map(item -> new OrderedChange<>(item.getId(), item.getUpdatedAt(), item))
            .toList();
        return buildResponse("gtin", ordered, cursor, updatedAfter, requestedSize);
    }

    public SyncChangesResponse<Car> getCarChanges(String cursor, String updatedAfter, Integer requestedSize) {
        List<OrderedChange<Car>> ordered = carRepository.findAll().stream()
            .map(this::toCar)
            .map(item -> new OrderedChange<>(item.getId(), item.getUpdatedAt(), item))
            .toList();
        return buildResponse("car", ordered, cursor, updatedAfter, requestedSize);
    }

    public SyncChangesResponse<Android> getAndroidChanges(String cursor, String updatedAfter, Integer requestedSize) {
        List<OrderedChange<Android>> ordered = androidRepository.findAll().stream()
            .map(this::toAndroid)
            .map(item -> new OrderedChange<>(item.getId(), item.getUpdatedAt(), item))
            .toList();
        return buildResponse("android", ordered, cursor, updatedAfter, requestedSize);
    }

    public SyncChangesResponse<CarMileage> getCarMileageChanges(String cursor, String updatedAfter, Integer requestedSize) {
        List<OrderedChange<CarMileage>> ordered = carMileageRepository.findAll().stream()
            .map(this::toCarMileage)
            .map(item -> new OrderedChange<>(item.getId(), item.getUpdatedAt(), item))
            .toList();
        return buildResponse("car-mileage", ordered, cursor, updatedAfter, requestedSize);
    }

    private <T extends SyncUpdatedItem> SyncChangesResponse<T> buildResponse(
        String domain,
        List<OrderedChange<T>> allItems,
        String cursor,
        String updatedAfter,
        Integer requestedSize
    ) {
        int size = normalizeSize(requestedSize);
        SyncCursorCodec.CursorState state = cursorCodec.decode(cursor);
        Instant effectiveUpdatedAfter = resolveUpdatedAfter(state, updatedAfter);
        Instant windowEnd = resolveWindowEnd(state);

        List<OrderedChange<T>> filtered = allItems.stream()
            .filter(change -> change.updatedAt() != null)
            .filter(change -> change.updatedAt().isAfter(effectiveUpdatedAfter))
            .filter(change -> !change.updatedAt().isAfter(windowEnd))
            .sorted(Comparator
                .comparing(OrderedChange<T>::updatedAt)
                .thenComparing(OrderedChange<T>::id, Comparator.nullsLast(String::compareTo)))
            .toList();

        int fromIndex = Math.min(state.offset(), filtered.size());
        int toIndex = Math.min(fromIndex + size, filtered.size());

        List<T> pageItems = filtered.subList(fromIndex, toIndex).stream().map(OrderedChange::item).toList();
        boolean hasMore = toIndex < filtered.size();

        String nextCursor = hasMore
            ? cursorCodec.encode(toIndex, effectiveUpdatedAfter.toString(), windowEnd)
            : null;

        List<String> deletedIds = syncDeletionEventService.findDeletedIds(domain, effectiveUpdatedAfter, windowEnd);

        return new SyncChangesResponse<>(
            pageItems,
            deletedIds,
            nextCursor,
            hasMore,
            pageItems.size()
        );
    }

    private Instant resolveUpdatedAfter(SyncCursorCodec.CursorState state, String requestUpdatedAfter) {
        if (state.updatedAfter() != null) {
            return parseInstant(state.updatedAfter(), "cursor.updatedAfter");
        }

        if (requestUpdatedAfter == null || requestUpdatedAfter.isBlank()) {
            return Instant.EPOCH;
        }

        return parseInstant(requestUpdatedAfter, "updatedAfter");
    }

    private Instant resolveWindowEnd(SyncCursorCodec.CursorState state) {
        if (state.windowEnd() != null) {
            return state.windowEnd();
        }

        return Instant.now();
    }

    private Instant parseInstant(String value, String fieldName) {
        try {
            return Instant.parse(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Le parametre " + fieldName + " doit etre au format ISO-8601.", ex);
        }
    }

    private Gtin toGtin(GtinEntity entity) {
        Gtin model = new Gtin();
        model.setId(entity.getId());
        model.setCode(entity.getCode());
        model.setDescription(entity.getDescription());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    private Car toCar(CarEntity entity) {
        Car model = new Car();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setVehicleRegistrationPlate(entity.getVehicleRegistrationPlate());
        model.setDescription(entity.getDescription());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    private Android toAndroid(AndroidEntity entity) {
        Android model = new Android();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setPackageName(entity.getPackageName());
        model.setCategory(entity.getCategory());
        model.setDescription(entity.getDescription());
        model.setIcon(entity.getIcon());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    private CarMileage toCarMileage(CarMileageEntity entity) {
        CarMileage model = new CarMileage();
        model.setId(entity.getId());
        model.setCarId(entity.getCar().getId());
        model.setCarName(entity.getCar().getName());
        model.setReadingAt(entity.getReadingAt());
        model.setOdometerKm(entity.getOdometerKm());
        model.setFuelVolumeLiters(entity.getFuelVolumeLiters());
        model.setFullTank(entity.isFullTank());
        model.setUpdatedAt(entity.getUpdatedAt());
        return model;
    }

    private int normalizeSize(Integer requestedSize) {
        if (requestedSize == null) {
            return DEFAULT_SIZE;
        }

        if (requestedSize <= 0) {
            throw new IllegalArgumentException("Le parametre size doit etre superieur a 0.");
        }

        return Math.min(requestedSize, MAX_SIZE);
    }

    private record OrderedChange<T extends SyncUpdatedItem>(String id, Instant updatedAt, T item) {
        private OrderedChange {
            Objects.requireNonNull(item, "item");
        }
    }
}
