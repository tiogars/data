import 'dart:async';

import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application/core/api/gateway_api_client.dart';
import 'package:flutter_application/core/api/mobile_runtime_config.dart';
import 'package:flutter_application/core/api/mobile_runtime_config_repository.dart';
import 'package:flutter_application/core/auth/auth_service.dart';
import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/sync/sync_engine.dart';
import 'package:flutter_application/core/sync/sync_process_result.dart';
import 'package:flutter_application/features/auth/presentation/login_page.dart';
import 'package:flutter_application/features/dashboard/presentation/dashboard_page.dart';
import 'package:provider/provider.dart';

class DataMobileApp extends StatefulWidget {
  const DataMobileApp({super.key});

  @override
  State<DataMobileApp> createState() => _DataMobileAppState();
}

class _DataMobileAppState extends State<DataMobileApp> {
  late final AuthService _authService;
  MobileRuntimeConfig _runtimeConfig = MobileRuntimeConfig.local;

  static const MobileRuntimeConfigRepository _runtimeConfigRepository =
      MobileRuntimeConfigRepository();
  static const SyncQueueRepository _syncQueueRepository = SyncQueueRepository();

  StreamSubscription<List<ConnectivityResult>>? _connectivitySubscription;
  Timer? _adaptiveSyncTimer;
  bool _isSyncRunning = false;

  @override
  void initState() {
    super.initState();

    debugPrint('[DataMobileApp] initState called');
    _authService = AuthService();
    _authService.addListener(_onAuthChanged);

    _init();

    _connectivitySubscription = Connectivity().onConnectivityChanged.listen((results) {
      if (results.contains(ConnectivityResult.none)) return;
      if (_authService.initialized) _triggerSync();
    });
  }

  void _onAuthChanged() {
    if (mounted) setState(() {});
  }

  /// Initialise auth + config en parallèle, puis déclenche le premier sync.
  Future<void> _init() async {
    try {
      debugPrint('[DataMobileApp] Starting initialization...');
      await Future.wait([
        _authService.initialize(),
        _loadRuntimeConfig(),
      ]);
      debugPrint('[DataMobileApp] Initialization complete');
      _triggerSync();
    } catch (e, st) {
      debugPrint('[DataMobileApp] Init error: $e');
      debugPrint('[DataMobileApp] StackTrace: $st');
    }
  }

  Future<void> _loadRuntimeConfig() async {
    final savedConfig = await _runtimeConfigRepository.load();
    if (!mounted) return;
    setState(() {
      _runtimeConfig = savedConfig;
    });
  }

  Future<void> _saveRuntimeConfig(MobileRuntimeConfig config) async {
    await _runtimeConfigRepository.save(config);
    if (!mounted) return;
    setState(() {
      _runtimeConfig = config;
    });
    await _triggerSync();
  }

  /// Retourne le token effectif :
  ///   - le JWT manuel (champ debug dans les paramètres) s'il est renseigné,
  ///   - sinon le token OIDC valide (avec refresh silencieux si besoin).
  Future<String?> _effectiveToken() async {
    final manual = _runtimeConfig.jwtToken;
    if (manual != null && manual.isNotEmpty) return manual;
    return _authService.validAccessToken();
  }

  /// Construit un SyncEngine avec un token fraîchement obtenu.
  Future<SyncEngine> _buildFreshEngine() async {
    final token = await _effectiveToken();
    final config = MobileRuntimeConfig(
      gatewayBaseUrl: _runtimeConfig.gatewayBaseUrl,
      jwtToken: token,
    );
    return SyncEngine(
      apiClient: GatewayApiClient(config: config),
      queueRepository: const SyncQueueRepository(),
    );
  }

  Future<SyncProcessResult> _triggerSync() async {
    if (_isSyncRunning) {
      return const SyncProcessResult(processed: 0, succeeded: 0, failed: 0);
    }

    _isSyncRunning = true;

    try {
      final engine = await _buildFreshEngine();
      return await engine.processQueue();
    } catch (_) {
      return const SyncProcessResult(processed: 0, succeeded: 0, failed: 0);
    } finally {
      _isSyncRunning = false;
      _scheduleNextAdaptiveSync();
    }
  }

  void _scheduleNextAdaptiveSync() {
    _adaptiveSyncTimer?.cancel();

    _nextAdaptiveInterval().then((interval) {
      if (!mounted) return;
      _adaptiveSyncTimer = Timer(interval, _triggerSync);
    });
  }

  Future<Duration> _nextAdaptiveInterval() async {
    final network = await Connectivity().checkConnectivity();
    if (network.contains(ConnectivityResult.none)) {
      return const Duration(minutes: 5);
    }

    final pending = await _syncQueueRepository.pendingCount();
    if (pending > 20) return const Duration(seconds: 30);
    if (pending > 0) return const Duration(minutes: 2);
    return const Duration(minutes: 5);
  }

  @override
  void dispose() {
    _authService.removeListener(_onAuthChanged);
    _connectivitySubscription?.cancel();
    _adaptiveSyncTimer?.cancel();
    super.dispose();
  }

  Widget _buildHome() {
    if (!_authService.initialized) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    // Mode dev : un JWT manuel dans les paramètres bypass le gate OIDC.
    final hasManualToken =
        _runtimeConfig.jwtToken != null && _runtimeConfig.jwtToken!.isNotEmpty;

    if (!_authService.isAuthenticated && !hasManualToken) {
      return const LoginPage();
    }

    return DashboardPage(
      onSyncNow: _triggerSync,
      runtimeConfig: _runtimeConfig,
      onSaveRuntimeConfig: _saveRuntimeConfig,
    );
  }

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider<AuthService>.value(
      value: _authService,
      child: MaterialApp(
        title: 'Data Mobile',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(seedColor: Colors.indigo),
        ),
        home: _buildHome(),
      ),
    );
  }
}

