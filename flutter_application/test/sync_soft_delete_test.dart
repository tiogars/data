import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:test/test.dart';
import 'package:flutter_application/core/api/gateway_api_client.dart';
import 'package:flutter_application/core/api/mobile_runtime_config.dart';
import 'package:flutter_application/core/database/deleted_records_repository.dart';
import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/database/sync_state_repository.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:flutter_application/core/sync/sync_engine.dart';
import 'package:flutter_application/core/sync/sync_operation.dart';
import 'package:mocktail/mocktail.dart';
import 'package:sqflite/sqflite.dart';

void main() {
  group('DeletedRecordsRepository', () {
    test('shouldCountAndPurgeDeletedRowsWhenTombstonesExist', () async {
      final database = _MockDatabase();
      when(() => database.rawQuery(any())).thenAnswer(
        (_) async => <Map<String, Object?>>[
          <String, Object?>{'count': 1},
        ],
      );
      when(() => database.delete(any(), where: any(named: 'where'))).thenAnswer((_) async => 1);

      final repository = DeletedRecordsRepository(databaseProvider: () async => database);

      expect(await repository.countDeletedRows(), 4);
      expect(await repository.purgeDeletedRows(), 4);

      verify(() => database.rawQuery('SELECT COUNT(*) as count FROM ${TableNames.gtin} WHERE deleted_at IS NOT NULL')).called(1);
      verify(() => database.rawQuery('SELECT COUNT(*) as count FROM ${TableNames.car} WHERE deleted_at IS NOT NULL')).called(1);
      verify(() => database.rawQuery('SELECT COUNT(*) as count FROM ${TableNames.carMileage} WHERE deleted_at IS NOT NULL')).called(1);
      verify(() => database.rawQuery('SELECT COUNT(*) as count FROM ${TableNames.androidApp} WHERE deleted_at IS NOT NULL')).called(1);
      verify(() => database.delete(TableNames.gtin, where: 'deleted_at IS NOT NULL')).called(1);
      verify(() => database.delete(TableNames.car, where: 'deleted_at IS NOT NULL')).called(1);
      verify(() => database.delete(TableNames.carMileage, where: 'deleted_at IS NOT NULL')).called(1);
      verify(() => database.delete(TableNames.androidApp, where: 'deleted_at IS NOT NULL')).called(1);
    });
  });

  group('SyncEngine', () {
    test('shouldMarkDeletedAtWhenIncrementalSyncReturnsDeletedIds', () async {
      final fakeTransaction = _FakeTransaction();
      final fakeDatabase = _FakeDatabase(fakeTransaction);
      final syncStateRepository = _MockSyncStateRepository();

      when(() => syncStateRepository.getCursor(any())).thenAnswer((_) async => null);
      when(() => syncStateRepository.setCursor(any(), any())).thenAnswer((_) async {});
      when(() => syncStateRepository.clearCursor(any())).thenAnswer((_) async {});

      final syncEngine = SyncEngine(
        apiClient: _FakeGatewayApiClient(
          gtinIncrementalBatch: const CursorSyncBatch(
            items: <Map<String, dynamic>>[],
            deletedIds: <String>['gtin-1'],
            nextCursor: 'cursor-1',
          ),
        ),
        queueRepository: const _FakeSyncQueueRepository(),
        connectivity: _FakeConnectivity(),
        syncStateRepository: syncStateRepository,
        databaseProvider: () async => fakeDatabase,
      );

      await syncEngine.processQueue();

      final gtinUpdates = fakeTransaction.updates
          .where((u) => u['table'] == TableNames.gtin && u['whereArgs']?.contains('gtin-1') == true)
          .toList();

      expect(gtinUpdates, hasLength(1));
      expect(gtinUpdates.first['values']['deleted_at'], isNotNull);
      expect(gtinUpdates.first['values']['is_dirty'], 0);
      verify(() => syncStateRepository.setCursor('gtin', 'cursor-1')).called(1);
    });
  });
}

class _FakeConnectivity implements Connectivity {
  @override
  Future<List<ConnectivityResult>> checkConnectivity() async => <ConnectivityResult>[ConnectivityResult.wifi];

  @override
  Stream<List<ConnectivityResult>> get onConnectivityChanged => const Stream<List<ConnectivityResult>>.empty();

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

class _FakeSyncQueueRepository extends SyncQueueRepository {
  const _FakeSyncQueueRepository();

  @override
  Future<List<SyncOperation>> listPending({int limit = 100}) async => const <SyncOperation>[];

  @override
  Future<void> removeById(int id) async {}

  @override
  Future<void> incrementAttempts(int id) async {}
}

class _FakeGatewayApiClient extends GatewayApiClient {
  _FakeGatewayApiClient({this.gtinIncrementalBatch}) : super(config: MobileRuntimeConfig.local);

  final CursorSyncBatch? gtinIncrementalBatch;

  @override
  Future<CursorSyncBatch?> fetchGtinItemsIncremental({String? cursor}) async => gtinIncrementalBatch;

  @override
  Future<CursorSyncBatch?> fetchCarItemsIncremental({String? cursor}) async => const CursorSyncBatch(items: <Map<String, dynamic>>[]);

  @override
  Future<CursorSyncBatch?> fetchCarMileageItemsIncremental({String? cursor, int pageSize = 100}) async =>
      const CursorSyncBatch(items: <Map<String, dynamic>>[]);

  @override
  Future<CursorSyncBatch?> fetchAndroidItemsIncremental({String? cursor}) async => const CursorSyncBatch(items: <Map<String, dynamic>>[]);

  @override
  Future<List<Map<String, dynamic>>> fetchGtinItems() async => const <Map<String, dynamic>>[];

  @override
  Future<List<Map<String, dynamic>>> fetchCarItems() async => const <Map<String, dynamic>>[];

  @override
  Future<List<Map<String, dynamic>>> fetchCarMileageItems({int pageSize = 100}) async => const <Map<String, dynamic>>[];

  @override
  Future<List<Map<String, dynamic>>> fetchAndroidItems() async => const <Map<String, dynamic>>[];

  @override
  Future<void> postDomainOperation({
    required String domain,
    required String operationType,
    required Map<String, dynamic> payload,
    int? entityId,
  }) async {}
}

class _MockDatabase extends Mock implements Database {}

class _MockSyncStateRepository extends Mock implements SyncStateRepository {}

class _FakeTransaction extends Fake implements Transaction {
  final List<Map<String, dynamic>> updates = [];

  @override
  Future<int> update(
    String table,
    Map<String, Object?> values, {
    String? where,
    List<Object?>? whereArgs,
    ConflictAlgorithm? conflictAlgorithm,
  }) async {
    updates.add({'table': table, 'values': values, 'where': where, 'whereArgs': whereArgs});
    return 1;
  }

  @override
  Future<int> insert(
    String table,
    Map<String, Object?> values, {
    String? nullColumnHack,
    ConflictAlgorithm? conflictAlgorithm,
  }) async =>
      0;

  @override
  Future<int> delete(String table, {String? where, List<Object?>? whereArgs}) async => 0;

  @override
  Future<List<Map<String, Object?>>> query(
    String table, {
    bool? distinct,
    List<String>? columns,
    String? where,
    List<Object?>? whereArgs,
    String? groupBy,
    String? having,
    String? orderBy,
    int? limit,
    int? offset,
  }) async =>
      [];

  @override
  Future<List<Map<String, Object?>>> rawQuery(String sql, [List<Object?>? arguments]) async => [];

  @override
  Future<int> rawUpdate(String sql, [List<Object?>? arguments]) async => 0;

  @override
  Future<int> rawInsert(String sql, [List<Object?>? arguments]) async => 0;

  @override
  Future<int> rawDelete(String sql, [List<Object?>? arguments]) async => 0;
}

class _FakeDatabase extends Fake implements Database {
  _FakeDatabase(this._transaction);

  final _FakeTransaction _transaction;

  @override
  Future<T> transaction<T>(Future<T> Function(Transaction txn) action, {bool? exclusive}) async {
    return await action(_transaction);
  }
}
