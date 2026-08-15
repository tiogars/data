import 'package:flutter_application/core/database/database_provider.dart';
import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:flutter_application/core/sync/sync_domains.dart';
import 'package:flutter_application/core/sync/sync_operation.dart';
import 'package:flutter_application/features/vehicles/domain/car_mileage_entry.dart';
import 'package:sqflite/sqflite.dart';

class CarMileageLocalRepository {
  const CarMileageLocalRepository();

  static const SyncQueueRepository _syncQueueRepository = SyncQueueRepository();

  Future<int> upsert(CarMileageEntry entry, {DatabaseExecutor? executor}) async {
    final db = executor ?? await DatabaseProvider.instance.database;
    return db.insert(
      TableNames.carMileage,
      {
        ...entry.toMap(),
        'deleted_at': null,
      },
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  Future<List<CarMileageEntry>> findByCarId(String carId) async {
    final db = await DatabaseProvider.instance.database;
    final rows = await db.query(
      TableNames.carMileage,
      where: 'car_id = ? AND deleted_at IS NULL',
      whereArgs: [carId],
      orderBy: 'reading_at DESC',
    );
    return rows.map(CarMileageEntry.fromMap).toList();
  }

  Future<void> saveOffline(CarMileageEntry entry) async {
    final db = await DatabaseProvider.instance.database;
    await db.transaction((txn) async {
      await upsert(
        CarMileageEntry(
          id: entry.id,
          carId: entry.carId,
          readingAt: entry.readingAt,
          odometerKm: entry.odometerKm,
          fuelVolumeLiters: entry.fuelVolumeLiters,
          fullTank: entry.fullTank,
          updatedAt: DateTime.now().toUtc(),
          isDirty: true,
        ),
        executor: txn,
      );

      await _syncQueueRepository.enqueue(
        SyncOperation(
          domain: SyncDomains.carMileage,
          operationType: entry.id == null ? 'create' : 'update',
          entityId: entry.id,
          payload: {
            'carId': entry.carId,
            'readingAt': entry.readingAt.toIso8601String(),
            'odometerKm': entry.odometerKm,
            'fuelVolumeLiters': entry.fuelVolumeLiters,
            'fullTank': entry.fullTank,
          },
          createdAt: DateTime.now().toUtc(),
        ),
        executor: txn,
      );
    });
  }

  Future<void> deleteOffline(CarMileageEntry entry) async {
    final db = await DatabaseProvider.instance.database;
    await db.transaction((txn) async {
      await txn.update(
        TableNames.carMileage,
        {'deleted_at': DateTime.now().toUtc().toIso8601String()},
        where: 'id = ?',
        whereArgs: [entry.id],
      );
      await _syncQueueRepository.enqueue(
        SyncOperation(
          domain: SyncDomains.carMileage,
          operationType: 'delete',
          entityId: entry.id,
          payload: {'id': entry.id},
          createdAt: DateTime.now().toUtc(),
        ),
        executor: txn,
      );
    });
  }

  Future<List<CarMileageEntry>> findAll() async {
    final db = await DatabaseProvider.instance.database;
    final rows = await db.query(
      TableNames.carMileage,
      where: 'deleted_at IS NULL',
      orderBy: 'reading_at DESC',
    );
    return rows.map(CarMileageEntry.fromMap).toList();
  }
}
