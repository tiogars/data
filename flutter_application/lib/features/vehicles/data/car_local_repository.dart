import 'package:flutter_application/core/database/database_provider.dart';
import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:flutter_application/core/sync/sync_domains.dart';
import 'package:flutter_application/core/sync/sync_operation.dart';
import 'package:flutter_application/features/vehicles/domain/car_item.dart';
import 'package:sqflite/sqflite.dart';

class CarLocalRepository {
  const CarLocalRepository();

  static const SyncQueueRepository _syncQueueRepository = SyncQueueRepository();

  Future<int> upsert(CarItem item) async {
    final db = await DatabaseProvider.instance.database;
    return db.insert(
      TableNames.car,
      {
        ...item.toMap(),
        'deleted_at': null,
      },
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  Future<List<CarItem>> findAll() async {
    final db = await DatabaseProvider.instance.database;
    final rows = await db.query(
      TableNames.car,
      where: 'deleted_at IS NULL',
      orderBy: 'name ASC',
    );
    return rows.map(CarItem.fromMap).toList();
  }

  Future<void> saveOffline(CarItem item) async {
    await upsert(
      CarItem(
        id: item.id,
        name: item.name,
        plateNumber: item.plateNumber,
        updatedAt: DateTime.now().toUtc(),
        isDirty: true,
      ),
    );

    await _syncQueueRepository.enqueue(
      SyncOperation(
        domain: SyncDomains.car,
        operationType: item.id == null ? 'create' : 'update',
        entityId: item.id,
        payload: {
          'name': item.name,
          'vehicleRegistrationPlate': item.plateNumber,
          'description': null,
        },
        createdAt: DateTime.now().toUtc(),
      ),
    );
  }

  Future<void> deleteOffline(CarItem item) async {
    final db = await DatabaseProvider.instance.database;
    await db.update(
      TableNames.car,
      {'deleted_at': DateTime.now().toUtc().toIso8601String()},
      where: 'id = ?',
      whereArgs: [item.id],
    );
    await _syncQueueRepository.enqueue(
      SyncOperation(
        domain: SyncDomains.car,
        operationType: 'delete',
        entityId: item.id,
        payload: {'id': item.id},
        createdAt: DateTime.now().toUtc(),
      ),
    );
  }
}
