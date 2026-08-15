package fr.tiogars.data.sync.services;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.tiogars.data.products.gtin.entities.GtinEntity;
import fr.tiogars.data.products.gtin.models.Gtin;
import fr.tiogars.data.products.gtin.repositories.GtinRepository;
import fr.tiogars.data.softwares.android.entities.AndroidEntity;
import fr.tiogars.data.softwares.android.models.Android;
import fr.tiogars.data.softwares.android.repositories.AndroidRepository;
import fr.tiogars.data.softwares.winget.entities.WingetEntity;
import fr.tiogars.data.softwares.winget.models.Winget;
import fr.tiogars.data.softwares.winget.repositories.WingetRepository;
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
    private final WingetRepository wingetRepository;
    private final CarMileageRepository carMileageRepository;
    private final SyncCursorCodec cursorCodec;
    private final SyncDeletionEventService syncDeletionEventService;

    public SyncChangesService(
        GtinRepository gtinRepository,
        CarRepository carRepository,
        AndroidRepository androidRepository,
        WingetRepository wingetRepository,
        CarMileageRepository carMileageRepository,
        SyncCursorCodec cursorCodec,
        SyncDeletionEventService syncDeletionEventService
    ) {
        this.gtinRepository = gtinRepository;
        this.carRepository = carRepository;
        this.androidRepository = androidRepository;
        this.wingetRepository = wingetRepository;
        this.carMileageRepository = carMileageRepository;
        this.cursorCodec = cursorCodec;
        this.syncDeletionEventService = syncDeletionEventService;
    }

    public SyncChangesResponse<Gtin> getGtinChanges(String cursor, String updatedAfter, Integer requestedSize) {
        return buildResponse("gtin", gtinRepository::findSyncPage, this::toGtin, cursor, updatedAfter, requestedSize);
    }

    public SyncChangesResponse<Car> getCarChanges(String cursor, String updatedAfter, Integer requestedSize) {
        return buildResponse("car", carRepository::findSyncPage, this::toCar, cursor, updatedAfter, requestedSize);
    }

    public SyncChangesResponse<Android> getAndroidChanges(String cursor, String updatedAfter, Integer requestedSize) {
        return buildResponse("android", androidRepository::findSyncPage, this::toAndroid, cursor, updatedAfter, requestedSize);
    }

    public SyncChangesResponse<Winget> getWingetChanges(String cursor, String updatedAfter, Integer requestedSize) {
        return buildResponse("winget", wingetRepository::findSyncPage, this::toWinget, cursor, updatedAfter, requestedSize);
    }

    public SyncChangesResponse<CarMileage> getCarMileageChanges(String cursor, String updatedAfter, Integer requestedSize) {
        return buildResponse(
            "car-mileage",
            carMileageRepository::findSyncPage,
            this::toCarMileage,
            cursor,
            updatedAfter,
            requestedSize
        );
    }

    private <E, T extends SyncUpdatedItem> SyncChangesResponse<T> buildResponse(
        String domain,
        SyncPageFetcher<E> fetcher,
        Function<E, T> mapper,
        String cursor,
        String updatedAfter,
        Integer requestedSize
    ) {
        int size = normalizeSize(requestedSize);
        SyncCursorCodec.CursorState state = cursorCodec.decode(cursor);
        Instant effectiveUpdatedAfter = resolveUpdatedAfter(state, updatedAfter);
        Instant windowEnd = resolveWindowEnd(state);

        Instant lastUpdatedAt = state.lastUpdatedAt() == null ? effectiveUpdatedAfter : state.lastUpdatedAt();
        String lastId = state.lastId() == null ? "" : state.lastId();

        // On lit une ligne de plus que demande pour savoir s'il reste des changements.
        Pageable pageable = PageRequest.of(0, size + 1);
        List<E> fetched = fetcher.fetch(effectiveUpdatedAfter, windowEnd, lastUpdatedAt, lastId, pageable);

        boolean hasMore = fetched.size() > size;
        List<T> pageItems = fetched.stream()
            .limit(size)
            .map(mapper)
            .toList();

        String nextCursor = null;
        if (hasMore && !pageItems.isEmpty()) {
            T last = pageItems.get(pageItems.size() - 1);
            nextCursor = cursorCodec.encode(last.getUpdatedAt(), last.getId(), effectiveUpdatedAfter.toString(), windowEnd);
        }

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

    private Winget toWinget(WingetEntity entity) {
        Winget model = new Winget();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setWingetId(entity.getWingetId());
        model.setInstallCommand(entity.getInstallCommand());
        model.setTags(entity.getTags());
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

    @FunctionalInterface
    private interface SyncPageFetcher<E> {
        List<E> fetch(Instant updatedAfter, Instant windowEnd, Instant lastUpdatedAt, String lastId, Pageable pageable);
    }
}
