import 'package:flutter_application/core/database/database_provider.dart';
import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:flutter_application/core/sync/sync_domains.dart';
import 'package:flutter_application/core/sync/sync_operation.dart';
import 'package:flutter_application/features/android_apps/domain/android_app_item.dart';
import 'package:sqflite/sqflite.dart';

class AndroidAppLocalRepository {
  const AndroidAppLocalRepository();

  static const SyncQueueRepository _syncQueueRepository = SyncQueueRepository();

  Future<int> upsert(AndroidAppItem app) async {
    final db = await DatabaseProvider.instance.database;
    return db.insert(
      TableNames.androidApp,
      {
        ...app.toMap(),
        'deleted_at': null,
      },
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  Future<List<AndroidAppItem>> findAll() async {
    final db = await DatabaseProvider.instance.database;
    final rows = await db.query(
      TableNames.androidApp,
      where: 'deleted_at IS NULL',
      orderBy: 'name ASC',
    );
    return rows.map(AndroidAppItem.fromMap).toList();
  }

  Future<void> saveOffline(AndroidAppItem app) async {
    await upsert(
      AndroidAppItem(
        id: app.id,
        name: app.name,
        packageName: app.packageName,
        category: app.category,
        description: app.description,
        updatedAt: DateTime.now().toUtc(),
        isDirty: true,
      ),
    );

    await _syncQueueRepository.enqueue(
      SyncOperation(
        domain: SyncDomains.android,
        operationType: app.id == null ? 'create' : 'update',
        entityId: app.id,
        payload: {
          'name': app.name,
          'packageName': app.packageName,
          'category': app.category == null || app.category!.trim().isEmpty
              ? <String>[]
              : app.category!
                    .split(',')
                    .map((value) => value.trim())
                    .where((value) => value.isNotEmpty)
                    .toList(),
          'description': app.description,
          'icon': null,
        },
        createdAt: DateTime.now().toUtc(),
      ),
    );
  }

  Future<void> deleteOffline(AndroidAppItem app) async {
    final db = await DatabaseProvider.instance.database;
    await db.update(
      TableNames.androidApp,
      {'deleted_at': DateTime.now().toUtc().toIso8601String()},
      where: 'id = ?',
      whereArgs: [app.id],
    );
    await _syncQueueRepository.enqueue(
      SyncOperation(
        domain: SyncDomains.android,
        operationType: 'delete',
        entityId: app.id,
        payload: {'id': app.id},
        createdAt: DateTime.now().toUtc(),
      ),
    );
  }
}
