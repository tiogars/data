import 'dart:convert';

import 'package:flutter_application/core/database/database_provider.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:flutter_application/core/sync/sync_operation.dart';

class SyncQueueRepository {
  const SyncQueueRepository();

  Future<int> enqueue(SyncOperation operation) async {
    final db = await DatabaseProvider.instance.database;
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
    final db = await DatabaseProvider.instance.database;
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
    final db = await DatabaseProvider.instance.database;
    final result = await db.rawQuery('SELECT COUNT(*) as count FROM ${TableNames.syncQueue}');
    return (result.first['count'] as int?) ?? 0;
  }

  Future<int> pendingCountByDomain(String domain) async {
    final db = await DatabaseProvider.instance.database;
    final result = await db.rawQuery(
      'SELECT COUNT(*) as count FROM ${TableNames.syncQueue} WHERE domain = ?',
      [domain],
    );
    return (result.first['count'] as int?) ?? 0;
  }

  Future<void> removeById(int id) async {
    final db = await DatabaseProvider.instance.database;
    await db.delete(TableNames.syncQueue, where: 'id = ?', whereArgs: [id]);
  }

  Future<void> incrementAttempts(int id) async {
    final db = await DatabaseProvider.instance.database;
    await db.rawUpdate(
      'UPDATE ${TableNames.syncQueue} SET attempts = attempts + 1 WHERE id = ?',
      [id],
    );
  }
}
