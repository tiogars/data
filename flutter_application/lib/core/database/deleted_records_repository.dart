import 'package:flutter_application/core/database/database_provider.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:sqflite/sqflite.dart';

class DeletedRecordsRepository {
  const DeletedRecordsRepository({this.databaseProvider});

  final Future<Database> Function()? databaseProvider;

  Future<int> countDeletedRows() async {
    final db = await _database();

    final gtin = await _countByTable(db, TableNames.gtin);
    final car = await _countByTable(db, TableNames.car);
    final carMileage = await _countByTable(db, TableNames.carMileage);
    final androidApp = await _countByTable(db, TableNames.androidApp);

    return gtin + car + carMileage + androidApp;
  }

  Future<int> purgeDeletedRows() async {
    final db = await _database();

    var deleted = 0;
    deleted += await _purgeByTable(db, TableNames.gtin);
    deleted += await _purgeByTable(db, TableNames.car);
    deleted += await _purgeByTable(db, TableNames.carMileage);
    deleted += await _purgeByTable(db, TableNames.androidApp);

    return deleted;
  }

  Future<int> _countByTable(Database db, String table) async {
    final result = await db.rawQuery(
      'SELECT COUNT(*) as count FROM $table WHERE deleted_at IS NOT NULL',
    );

    return (result.first['count'] as int?) ?? 0;
  }

  Future<int> _purgeByTable(Database db, String table) async {
    return db.delete(
      table,
      where: 'deleted_at IS NOT NULL',
    );
  }

  Future<Database> _database() {
    return databaseProvider?.call() ?? DatabaseProvider.instance.database;
  }
}
