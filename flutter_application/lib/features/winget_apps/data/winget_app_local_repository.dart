import 'package:flutter_application/core/database/database_provider.dart';
import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:flutter_application/core/sync/sync_domains.dart';
import 'package:flutter_application/core/sync/sync_operation.dart';
import 'package:flutter_application/features/winget_apps/domain/winget_app_item.dart';
import 'package:sqflite/sqflite.dart';

class WingetAppLocalRepository {
  const WingetAppLocalRepository();

  static const SyncQueueRepository _syncQueueRepository = SyncQueueRepository();

  Future<int> upsert(WingetAppItem app, {DatabaseExecutor? executor}) async {
    final db = executor ?? await DatabaseProvider.instance.database;
    return db.insert(
      TableNames.wingetApp,
      {
        ...app.toMap(),
        'deleted_at': null,
      },
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  Future<List<WingetAppItem>> findAll() async {
    final db = await DatabaseProvider.instance.database;
    final rows = await db.query(
      TableNames.wingetApp,
      where: 'deleted_at IS NULL',
      orderBy: 'name ASC',
    );
    return rows.map(WingetAppItem.fromMap).toList();
  }

  Future<void> saveOffline(WingetAppItem app) async {
    final db = await DatabaseProvider.instance.database;
    await db.transaction((txn) async {
      await upsert(
        WingetAppItem(
          id: app.id,
          name: app.name,
          description: app.description,
          wingetId: app.wingetId,
          installCommand: app.installCommand,
          tags: app.tags,
          updatedAt: DateTime.now().toUtc(),
          isDirty: true,
        ),
        executor: txn,
      );

      await _syncQueueRepository.enqueue(
        SyncOperation(
          domain: SyncDomains.winget,
          operationType: app.id == null ? 'create' : 'update',
          entityId: app.id,
          payload: {
            'name': app.name,
            'description': app.description,
            'wingetId': app.wingetId,
            'installCommand': app.installCommand,
            'tags': app.tags == null || app.tags!.trim().isEmpty
                ? <String>[]
                : app.tags!
                      .split(',')
                      .map((value) => value.trim())
                      .where((value) => value.isNotEmpty)
                      .toList(),
          },
          createdAt: DateTime.now().toUtc(),
        ),
        executor: txn,
      );
    });
  }

  Future<void> deleteOffline(WingetAppItem app) async {
    final db = await DatabaseProvider.instance.database;
    await db.transaction((txn) async {
      await txn.update(
        TableNames.wingetApp,
        {'deleted_at': DateTime.now().toUtc().toIso8601String()},
        where: 'id = ?',
        whereArgs: [app.id],
      );
      await _syncQueueRepository.enqueue(
        SyncOperation(
          domain: SyncDomains.winget,
          operationType: 'delete',
          entityId: app.id,
          payload: {'id': app.id},
          createdAt: DateTime.now().toUtc(),
        ),
        executor: txn,
      );
    });
  }
}
