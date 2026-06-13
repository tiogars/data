import 'package:flutter_application/core/database/database_provider.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:sqflite/sqflite.dart';

class SyncStateRepository {
  const SyncStateRepository();

  Future<String?> getCursor(String domain) async {
    final db = await DatabaseProvider.instance.database;
    final rows = await db.query(
      TableNames.syncState,
      columns: ['last_cursor'],
      where: 'domain = ?',
      whereArgs: [domain],
      limit: 1,
    );

    if (rows.isEmpty) {
      return null;
    }

    return rows.first['last_cursor'] as String?;
  }

  Future<void> setCursor(String domain, String cursor) async {
    final db = await DatabaseProvider.instance.database;
    await db.insert(
      TableNames.syncState,
      {
        'domain': domain,
        'last_cursor': cursor,
      },
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
  }

  Future<void> clearCursor(String domain) async {
    final db = await DatabaseProvider.instance.database;
    await db.delete(
      TableNames.syncState,
      where: 'domain = ?',
      whereArgs: [domain],
    );
  }
}
