import 'package:flutter_application/core/database/database_provider.dart';
import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:flutter_application/core/sync/sync_domains.dart';
import 'package:flutter_application/core/sync/sync_operation.dart';
import 'package:flutter_application/features/gtin/domain/gtin_item.dart';
import 'package:sqflite/sqflite.dart';

class GtinLocalRepository {
  const GtinLocalRepository();

  static const SyncQueueRepository _syncQueueRepository = SyncQueueRepository();

  Future<int> upsert(GtinItem item) async {
    final db = await DatabaseProvider.instance.database;
    return db.insert(
      TableNames.gtin,
      {
        ...item.toMap(),
        'deleted_at': null,
      },
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  Future<List<GtinItem>> findAll() async {
    final db = await DatabaseProvider.instance.database;
    final rows = await db.query(
      TableNames.gtin,
      where: 'deleted_at IS NULL',
      orderBy: 'code ASC',
    );
    return rows.map(GtinItem.fromMap).toList();
  }

  Future<void> saveOffline(GtinItem item) async {
    await upsert(
      GtinItem(
        id: item.id,
        code: item.code,
        description: item.description,
        updatedAt: DateTime.now().toUtc(),
        isDirty: true,
      ),
    );

    await _syncQueueRepository.enqueue(
      SyncOperation(
        domain: SyncDomains.gtin,
        operationType: item.id == null ? 'create' : 'update',
        entityId: item.id,
        payload: {
          'code': item.code,
          'description': item.description,
        },
        createdAt: DateTime.now().toUtc(),
      ),
    );
  }
}
