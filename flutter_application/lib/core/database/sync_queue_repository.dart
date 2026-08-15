import 'dart:convert';

import 'package:flutter_application/core/database/database_provider.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:flutter_application/core/sync/sync_operation.dart';
import 'package:sqflite/sqflite.dart';

class SyncQueueRepository {
  const SyncQueueRepository({this.databaseProvider});

  final Future<Database> Function()? databaseProvider;

  /// [executor] permet de partager la transaction de la mutation locale
  /// afin que la donnée et son opération de synchronisation soient atomiques.
  Future<int> enqueue(SyncOperation operation, {DatabaseExecutor? executor}) async {
    final db = executor ?? await _database();
    return db.insert(
      TableNames.syncQueue,
      {
        'domain': operation.domain,
        'operation_type': operation.operationType,
        'entity_id': operation.entityId,
        'payload_json': jsonEncode(operation.payload),
        'created_at': operation.createdAt.toIso8601String(),
        'attempts': operation.attempts,
      },
    );
  }

  Future<List<SyncOperation>> listPending({int limit = 100}) async {
    final db = await _database();
    final rows = await db.query(
      TableNames.syncQueue,
      orderBy: 'created_at ASC',
      limit: limit,
    );

    return rows
        .map(
          (row) => SyncOperation(
            id: row['id'] as int?,
            domain: row['domain'] as String,
            operationType: row['operation_type'] as String,
            entityId: row['entity_id'] as int?,
            payload: (jsonDecode(row['payload_json'] as String) as Map<String, dynamic>),
            createdAt: DateTime.parse(row['created_at'] as String),
            attempts: row['attempts'] as int? ?? 0,
          ),
        )
        .toList();
  }

  Future<int> pendingCount() async {
    final db = await _database();
    final result = await db.rawQuery('SELECT COUNT(*) as count FROM ${TableNames.syncQueue}');
    return (result.first['count'] as int?) ?? 0;
  }

  Future<int> pendingCountByDomain(String domain) async {
    final db = await _database();
    final result = await db.rawQuery(
      'SELECT COUNT(*) as count FROM ${TableNames.syncQueue} WHERE domain = ?',
      [domain],
    );
    return (result.first['count'] as int?) ?? 0;
  }

  Future<void> removeById(int id) async {
    final db = await _database();
    await db.delete(TableNames.syncQueue, where: 'id = ?', whereArgs: [id]);
  }

  Future<void> incrementAttempts(int id) async {
    final db = await _database();
    await db.rawUpdate(
      'UPDATE ${TableNames.syncQueue} SET attempts = attempts + 1 WHERE id = ?',
      [id],
    );
  }

  Future<Database> _database() {
    return databaseProvider?.call() ?? DatabaseProvider.instance.database;
  }
}
