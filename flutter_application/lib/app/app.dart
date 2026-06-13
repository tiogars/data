import 'dart:async';

import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application/core/api/gateway_api_client.dart';
import 'package:flutter_application/core/api/mobile_runtime_config.dart';
import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/sync/sync_engine.dart';
import 'package:flutter_application/core/sync/sync_process_result.dart';
import 'package:flutter_application/features/dashboard/presentation/dashboard_page.dart';

class DataMobileApp extends StatefulWidget {
  const DataMobileApp({super.key});

  @override
  State<DataMobileApp> createState() => _DataMobileAppState();
}

class _DataMobileAppState extends State<DataMobileApp> {
  late final SyncEngine _syncEngine;
  static const SyncQueueRepository _syncQueueRepository = SyncQueueRepository();
  StreamSubscription<List<ConnectivityResult>>? _connectivitySubscription;
  Timer? _adaptiveSyncTimer;
  bool _isSyncRunning = false;

  @override
  void initState() {
    super.initState();

    _syncEngine = SyncEngine(
      apiClient: GatewayApiClient(config: MobileRuntimeConfig.local),
      queueRepository: const SyncQueueRepository(),
    );

    _triggerSync();

    _connectivitySubscription = Connectivity().onConnectivityChanged.listen((results) {
      if (results.contains(ConnectivityResult.none)) {
        return;
      }
      _triggerSync();
    });
  }

  Future<SyncProcessResult> _triggerSync() async {
    if (_isSyncRunning) {
      return const SyncProcessResult(processed: 0, succeeded: 0, failed: 0);
    }

    _isSyncRunning = true;

    try {
      return await _syncEngine.processQueue();
    } catch (_) {
      // Ignore sync failures in app bootstrap; queue retry remains available.
      return const SyncProcessResult(processed: 0, succeeded: 0, failed: 0);
    } finally {
      _isSyncRunning = false;
      _scheduleNextAdaptiveSync();
    }
  }

  void _scheduleNextAdaptiveSync() {
    _adaptiveSyncTimer?.cancel();

    _nextAdaptiveInterval().then((interval) {
      if (!mounted) {
        return;
      }

      _adaptiveSyncTimer = Timer(interval, () {
        _triggerSync();
      });
    });
  }

  Future<Duration> _nextAdaptiveInterval() async {
    final network = await Connectivity().checkConnectivity();
    if (network.contains(ConnectivityResult.none)) {
      return const Duration(minutes: 5);
    }

    final pending = await _syncQueueRepository.pendingCount();
    if (pending > 20) {
      return const Duration(seconds: 30);
    }

    if (pending > 0) {
      return const Duration(minutes: 2);
    }

    return const Duration(minutes: 5);
  }

  @override
  void dispose() {
    _connectivitySubscription?.cancel();
    _adaptiveSyncTimer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Data Mobile',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.indigo),
      ),
      home: DashboardPage(onSyncNow: _triggerSync),
    );
  }
}
