import 'package:path/path.dart' as p;
import 'package:path_provider/path_provider.dart';
import 'package:sqflite/sqflite.dart';

import 'package:flutter_application/core/database/table_names.dart';

class DatabaseProvider {
  DatabaseProvider._();

  static final DatabaseProvider instance = DatabaseProvider._();

  Database? _database;

  Future<Database> get database async {
    if (_database != null) {
      return _database!;
    }
    _database = await _open();
    return _database!;
  }

  Future<Database> _open() async {
    final directory = await getApplicationDocumentsDirectory();
    final path = p.join(directory.path, 'data_mobile.db');

    return openDatabase(
      path,
      version: 4,
      onCreate: (db, version) async {
        await db.execute('''
          CREATE TABLE ${TableNames.gtin} (
            id INTEGER PRIMARY KEY,
            remote_id TEXT UNIQUE,
            code TEXT NOT NULL,
            description TEXT,
            updated_at TEXT NOT NULL,
            deleted_at TEXT,
            is_dirty INTEGER NOT NULL DEFAULT 0
          )
        ''');

        await db.execute('''
          CREATE TABLE ${TableNames.car} (
            id INTEGER PRIMARY KEY,
            remote_id TEXT UNIQUE,
            name TEXT NOT NULL,
            plate_number TEXT,
            updated_at TEXT NOT NULL,
            deleted_at TEXT,
            is_dirty INTEGER NOT NULL DEFAULT 0
          )
        ''');

        await db.execute('''
          CREATE TABLE ${TableNames.carMileage} (
            id INTEGER PRIMARY KEY,
            remote_id TEXT UNIQUE,
            car_id TEXT NOT NULL,
            reading_at TEXT NOT NULL,
            odometer_km REAL NOT NULL,
            fuel_volume_liters REAL,
            full_tank INTEGER NOT NULL DEFAULT 0,
            updated_at TEXT NOT NULL,
            deleted_at TEXT,
            is_dirty INTEGER NOT NULL DEFAULT 0
          )
        ''');

        await db.execute('''
          CREATE TABLE ${TableNames.androidApp} (
            id INTEGER PRIMARY KEY,
            remote_id TEXT UNIQUE,
            name TEXT NOT NULL,
            package_name TEXT NOT NULL,
            category TEXT,
            description TEXT,
            updated_at TEXT NOT NULL,
            deleted_at TEXT,
            is_dirty INTEGER NOT NULL DEFAULT 0
          )
        ''');

        await db.execute('''
          CREATE TABLE ${TableNames.wingetApp} (
            id INTEGER PRIMARY KEY,
            remote_id TEXT UNIQUE,
            name TEXT NOT NULL,
            description TEXT,
            winget_id TEXT NOT NULL,
            install_command TEXT NOT NULL,
            tags TEXT,
            updated_at TEXT NOT NULL,
            deleted_at TEXT,
            is_dirty INTEGER NOT NULL DEFAULT 0
          )
        ''');

        await db.execute('''
          CREATE TABLE ${TableNames.syncQueue} (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            domain TEXT NOT NULL,
            operation_type TEXT NOT NULL,
            entity_id INTEGER,
            payload_json TEXT NOT NULL,
            created_at TEXT NOT NULL,
            attempts INTEGER NOT NULL DEFAULT 0
          )
        ''');

        await db.execute('''
          CREATE TABLE ${TableNames.syncState} (
            domain TEXT PRIMARY KEY,
            last_cursor TEXT
          )
        ''');
      },
      onUpgrade: (db, oldVersion, newVersion) async {
        if (oldVersion < 2) {
          await db.execute('ALTER TABLE ${TableNames.gtin} ADD COLUMN remote_id TEXT');
          await db.execute('ALTER TABLE ${TableNames.car} ADD COLUMN remote_id TEXT');
          await db.execute('ALTER TABLE ${TableNames.carMileage} ADD COLUMN remote_id TEXT');
          await db.execute('ALTER TABLE ${TableNames.androidApp} ADD COLUMN remote_id TEXT');

          await db.execute(
            'CREATE UNIQUE INDEX IF NOT EXISTS idx_${TableNames.gtin}_remote_id ON ${TableNames.gtin}(remote_id)',
          );
          await db.execute(
            'CREATE UNIQUE INDEX IF NOT EXISTS idx_${TableNames.car}_remote_id ON ${TableNames.car}(remote_id)',
          );
          await db.execute(
            'CREATE UNIQUE INDEX IF NOT EXISTS idx_${TableNames.carMileage}_remote_id ON ${TableNames.carMileage}(remote_id)',
          );
          await db.execute(
            'CREATE UNIQUE INDEX IF NOT EXISTS idx_${TableNames.androidApp}_remote_id ON ${TableNames.androidApp}(remote_id)',
          );
        }

        if (oldVersion < 3) {
          await db.execute('ALTER TABLE ${TableNames.gtin} ADD COLUMN deleted_at TEXT');
          await db.execute('ALTER TABLE ${TableNames.car} ADD COLUMN deleted_at TEXT');
          await db.execute('ALTER TABLE ${TableNames.carMileage} ADD COLUMN deleted_at TEXT');
          await db.execute('ALTER TABLE ${TableNames.androidApp} ADD COLUMN deleted_at TEXT');
        }

        if (oldVersion < 4) {
          await db.execute('''
            CREATE TABLE ${TableNames.wingetApp} (
              id INTEGER PRIMARY KEY,
              remote_id TEXT UNIQUE,
              name TEXT NOT NULL,
              description TEXT,
              winget_id TEXT NOT NULL,
              install_command TEXT NOT NULL,
              tags TEXT,
              updated_at TEXT NOT NULL,
              deleted_at TEXT,
              is_dirty INTEGER NOT NULL DEFAULT 0
            )
          ''');
        }
      },
    );
  }
}
