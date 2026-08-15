import 'dart:convert';

import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:flutter_application/core/sync/sync_operation.dart';
import 'package:mocktail/mocktail.dart';
import 'package:sqflite/sqflite.dart';
import 'package:test/test.dart';

void main() {
  late _MockDatabase database;
  late SyncQueueRepository repository;

  setUp(() {
    database = _MockDatabase();
    repository = SyncQueueRepository(databaseProvider: () async => database);
  });

  test('shouldEnqueueSerializedOperationWhenExecutorIsProvided', () async {
    final createdAt = DateTime.utc(2026, 8, 15, 12, 30);
    final operation = SyncOperation(
      domain: 'gtin',
      operationType: 'upsert',
      entityId: 42,
      payload: const <String, dynamic>{'code': '1234567890123'},
      createdAt: createdAt,
      attempts: 2,
    );
    when(() => database.insert(any(), any())).thenAnswer((_) async => 7);

    final id = await repository.enqueue(operation, executor: database);

    expect(id, 7);
    final captured = verify(
      () => database.insert(TableNames.syncQueue, captureAny()),
    ).captured.single as Map<String, Object?>;
    expect(captured['domain'], 'gtin');
    expect(captured['operation_type'], 'upsert');
    expect(captured['entity_id'], 42);
    expect(jsonDecode(captured['payload_json']! as String), operation.payload);
    expect(captured['created_at'], createdAt.toIso8601String());
    expect(captured['attempts'], 2);
  });

  test('shouldListPendingOperationsWhenRowsExist', () async {
    when(
      () => database.query(
        any(),
        orderBy: any(named: 'orderBy'),
        limit: any(named: 'limit'),
      ),
    ).thenAnswer(
      (_) async => <Map<String, Object?>>[
        <String, Object?>{
          'id': 4,
          'domain': 'car',
          'operation_type': 'delete',
          'entity_id': null,
          'payload_json': '{"remoteId":"car-1"}',
          'created_at': '2026-08-15T10:00:00.000Z',
          'attempts': null,
        },
      ],
    );

    final operations = await repository.listPending(limit: 25);

    expect(operations, hasLength(1));
    expect(operations.single.id, 4);
    expect(operations.single.domain, 'car');
    expect(operations.single.operationType, 'delete');
    expect(operations.single.entityId, isNull);
    expect(operations.single.payload, <String, dynamic>{'remoteId': 'car-1'});
    expect(operations.single.createdAt, DateTime.parse('2026-08-15T10:00:00.000Z'));
    expect(operations.single.attempts, 0);
    verify(
      () => database.query(
        TableNames.syncQueue,
        orderBy: 'created_at ASC',
        limit: 25,
      ),
    ).called(1);
  });

  test('shouldReturnCountsWhenQueueContainsPendingOperations', () async {
    when(() => database.rawQuery(any(), any())).thenAnswer(
      (invocation) async {
        final sql = invocation.positionalArguments.first as String;
        return <Map<String, Object?>>[
          <String, Object?>{'count': sql.contains('WHERE domain') ? null : 3},
        ];
      },
    );

    expect(await repository.pendingCount(), 3);
    expect(await repository.pendingCountByDomain('gtin'), 0);
    verify(
      () => database.rawQuery(
        'SELECT COUNT(*) as count FROM ${TableNames.syncQueue} WHERE domain = ?',
        <Object?>['gtin'],
      ),
    ).called(1);
  });

  test('shouldRemoveOperationAndIncrementAttemptsWhenIdsAreProvided', () async {
    when(
      () => database.delete(
        any(),
        where: any(named: 'where'),
        whereArgs: any(named: 'whereArgs'),
      ),
    ).thenAnswer((_) async => 1);
    when(() => database.rawUpdate(any(), any())).thenAnswer((_) async => 1);

    await repository.removeById(8);
    await repository.incrementAttempts(9);

    verify(
      () => database.delete(
        TableNames.syncQueue,
        where: 'id = ?',
        whereArgs: <Object?>[8],
      ),
    ).called(1);
    verify(
      () => database.rawUpdate(
        'UPDATE ${TableNames.syncQueue} SET attempts = attempts + 1 WHERE id = ?',
        <Object?>[9],
      ),
    ).called(1);
  });
}

class _MockDatabase extends Mock implements Database {}