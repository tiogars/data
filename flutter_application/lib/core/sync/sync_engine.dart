import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:dio/dio.dart';

import 'package:flutter_application/core/api/gateway_api_client.dart';
import 'package:flutter_application/core/database/database_provider.dart';
import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/database/sync_state_repository.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:flutter_application/core/sync/sync_domains.dart';
import 'package:flutter_application/core/sync/sync_process_result.dart';
import 'package:sqflite/sqflite.dart';

class SyncEngine {
  SyncEngine({
    required this.apiClient,
    required this.queueRepository,
    Connectivity? connectivity,
    SyncStateRepository? syncStateRepository,
    Future<Database> Function()? databaseProvider,
  })  :
        connectivity = connectivity ?? Connectivity(),
        syncStateRepository = syncStateRepository ?? const SyncStateRepository(),
        databaseProvider = databaseProvider ?? _defaultDatabaseProvider;

  final GatewayApiClient apiClient;
  final SyncQueueRepository queueRepository;
  final Connectivity connectivity;
  final SyncStateRepository syncStateRepository;
  final Future<Database> Function() databaseProvider;

  static Future<Database> _defaultDatabaseProvider() => DatabaseProvider.instance.database;

  Future<SyncProcessResult> processQueue() async {
    final connectivityResult = await connectivity.checkConnectivity();
    if (connectivityResult.contains(ConnectivityResult.none)) {
      return const SyncProcessResult(processed: 0, succeeded: 0, failed: 0);
    }

    final operations = await queueRepository.listPending(limit: 50);
    var succeeded = 0;
    var failed = 0;
    final failedDomains = <String>{};

    for (final operation in operations) {
      try {
        await apiClient.postDomainOperation(
          domain: operation.domain,
          operationType: operation.operationType,
          payload: operation.payload,
          entityId: operation.entityId,
        );

        if (operation.id != null) {
          await queueRepository.removeById(operation.id!);
        }
        succeeded++;
      } catch (_) {
        if (operation.id != null) {
          await queueRepository.incrementAttempts(operation.id!);
        }
        failed++;
        failedDomains.add(operation.domain);
      }
    }

    await _refreshLocalCacheFromServer(failedDomains: failedDomains);

    return SyncProcessResult(
      processed: operations.length,
      succeeded: succeeded,
      failed: failed,
    );
  }

  Future<void> _refreshLocalCacheFromServer({required Set<String> failedDomains}) async {
    final now = DateTime.now().toUtc().toIso8601String();

    if (!failedDomains.contains(SyncDomains.gtin)) {
      await _syncDomainWithFallback(
        domain: SyncDomains.gtin,
        fetchIncremental: apiClient.fetchGtinItemsIncremental,
        fetchFull: apiClient.fetchGtinItems,
        applyBatch: (txn, items, deleteAll) async {
          if (deleteAll) {
            await txn.delete(TableNames.gtin);
          }
          for (final item in items) {
            await txn.insert(
              TableNames.gtin,
              {
                'remote_id': item['id'] as String?,
                'code': item['code'] as String? ?? '',
                'description': item['description'] as String? ?? '',
                'updated_at': now,
                'deleted_at': null,
                'is_dirty': 0,
              },
              conflictAlgorithm: ConflictAlgorithm.replace,
            );
          }
        },
      );
    }

    if (!failedDomains.contains(SyncDomains.car)) {
      await _syncDomainWithFallback(
        domain: SyncDomains.car,
        fetchIncremental: apiClient.fetchCarItemsIncremental,
        fetchFull: apiClient.fetchCarItems,
        applyBatch: (txn, items, deleteAll) async {
          if (deleteAll) {
            await txn.delete(TableNames.car);
          }
          for (final item in items) {
            await txn.insert(
              TableNames.car,
              {
                'remote_id': item['id'] as String?,
                'name': item['name'] as String? ?? '',
                'plate_number': item['vehicleRegistrationPlate'] as String?,
                'updated_at': now,
                'deleted_at': null,
                'is_dirty': 0,
              },
              conflictAlgorithm: ConflictAlgorithm.replace,
            );
          }
        },
      );
    }

    if (!failedDomains.contains(SyncDomains.carMileage)) {
      await _syncDomainWithFallback(
        domain: SyncDomains.carMileage,
        fetchIncremental: apiClient.fetchCarMileageItemsIncremental,
        fetchFull: apiClient.fetchCarMileageItems,
        applyBatch: (txn, items, deleteAll) async {
          if (deleteAll) {
            await txn.delete(TableNames.carMileage);
          }
          for (final item in items) {
            final fullTankValue = item['fullTank'];
            final isFullTank = fullTankValue == true || fullTankValue == 1;

            await txn.insert(
              TableNames.carMileage,
              {
                'remote_id': item['id'] as String?,
                'car_id': item['carId'] as String? ?? '',
                'reading_at': item['readingAt'] as String? ?? now,
                'odometer_km': item['odometerKm'] as int? ?? 0,
                'fuel_volume_liters': (item['fuelVolumeLiters'] as num?)?.toDouble(),
                'full_tank': isFullTank ? 1 : 0,
                'updated_at': now,
                'deleted_at': null,
                'is_dirty': 0,
              },
              conflictAlgorithm: ConflictAlgorithm.replace,
            );
          }
        },
      );
    }

    if (!failedDomains.contains(SyncDomains.android)) {
      await _syncDomainWithFallback(
        domain: SyncDomains.android,
        fetchIncremental: apiClient.fetchAndroidItemsIncremental,
        fetchFull: apiClient.fetchAndroidItems,
        applyBatch: (txn, items, deleteAll) async {
          if (deleteAll) {
            await txn.delete(TableNames.androidApp);
          }
          for (final item in items) {
            final rawCategory = item['category'];
            final category = rawCategory is List
                ? rawCategory.whereType<String>().join(', ')
                : (rawCategory as String?);

            await txn.insert(
              TableNames.androidApp,
              {
                'remote_id': item['id'] as String?,
                'name': item['name'] as String? ?? '',
                'package_name': item['packageName'] as String? ?? '',
                'category': category,
                'description': item['description'] as String?,
                'updated_at': now,
                'deleted_at': null,
                'is_dirty': 0,
              },
              conflictAlgorithm: ConflictAlgorithm.replace,
            );
          }
        },
      );
    }
  }

  Future<void> _syncDomainWithFallback({
    required String domain,
    required Future<CursorSyncBatch?> Function({String? cursor}) fetchIncremental,
    required Future<List<Map<String, dynamic>>> Function() fetchFull,
    required Future<void> Function(
      Transaction txn,
      List<Map<String, dynamic>> items,
      bool deleteAll,
    ) applyBatch,
  }) async {
    final db = await databaseProvider();

    try {
      final cursor = await syncStateRepository.getCursor(domain);
      final incrementalBatch = await fetchIncremental(cursor: cursor);

      if (incrementalBatch != null) {
        await db.transaction((txn) async {
          await applyBatch(txn, incrementalBatch.items, false);

          if (incrementalBatch.deletedIds.isNotEmpty) {
            for (final remoteId in incrementalBatch.deletedIds) {
              await txn.update(
                _tableForDomain(domain),
                {
                  'deleted_at': DateTime.now().toUtc().toIso8601String(),
                  'is_dirty': 0,
                },
                where: 'remote_id = ?',
                whereArgs: [remoteId],
              );
            }
          }
        });

        if (incrementalBatch.nextCursor != null && incrementalBatch.nextCursor!.isNotEmpty) {
          await syncStateRepository.setCursor(domain, incrementalBatch.nextCursor!);
        }
        return;
      }

      final fullItems = await fetchFull();
      await db.transaction((txn) async {
        await applyBatch(txn, fullItems, true);
      });
      await syncStateRepository.clearCursor(domain);
    } on DioException {
      // Keep local cache if network call for this domain fails.
    } catch (_) {
      // Keep local cache if mapping/storage for this domain fails.
    }
  }

  String _tableForDomain(String domain) {
    switch (domain) {
      case SyncDomains.gtin:
        return TableNames.gtin;
      case SyncDomains.car:
        return TableNames.car;
      case SyncDomains.carMileage:
        return TableNames.carMileage;
      case SyncDomains.android:
        return TableNames.androidApp;
      default:
        return TableNames.syncQueue;
    }
  }
}
