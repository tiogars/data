import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_application/core/database/deleted_records_repository.dart';
import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/sync/sync_domains.dart';
import 'package:flutter_application/core/sync/sync_process_result.dart';
import 'package:flutter_application/core/sync/sync_status.dart';
import 'package:flutter_application/core/api/mobile_runtime_config.dart';
import 'package:flutter_application/features/dashboard/presentation/gateway_settings_page.dart';
import 'package:flutter_application/features/dashboard/presentation/home_metrics_page.dart';
import 'package:flutter_application/features/dashboard/presentation/synchronizations_page.dart';
import 'package:flutter_application/features/android_apps/presentation/android_app_offline_form_page.dart';
import 'package:flutter_application/features/android_apps/presentation/android_app_list_page.dart';
import 'package:flutter_application/features/gtin/presentation/gtin_offline_form_page.dart';
import 'package:flutter_application/features/gtin/presentation/gtin_list_page.dart';
import 'package:flutter_application/features/vehicles/presentation/car_offline_form_page.dart';
import 'package:flutter_application/features/vehicles/presentation/car_list_page.dart';
import 'package:flutter_application/features/vehicles/presentation/car_mileage_offline_form_page.dart';
import 'package:flutter_application/features/winget_apps/presentation/winget_app_list_page.dart';
import 'package:flutter_application/features/winget_apps/presentation/winget_app_offline_form_page.dart';
import 'package:url_launcher/url_launcher.dart';

class DashboardPage extends StatefulWidget {
  const DashboardPage({
    super.key,
    required this.onSyncNow,
    required this.runtimeConfig,
    required this.onSaveRuntimeConfig,
  });

  final Future<SyncProcessResult> Function() onSyncNow;
  final MobileRuntimeConfig runtimeConfig;
  final Future<void> Function(MobileRuntimeConfig config) onSaveRuntimeConfig;

  @override
  State<DashboardPage> createState() => _DashboardPageState();
}

class _DashboardPageState extends State<DashboardPage> {
  int _refreshCounter = 0;
  int _selectedMenuIndex = 0;

  static const SyncQueueRepository _syncQueueRepository = SyncQueueRepository();
  static const DeletedRecordsRepository _deletedRecordsRepository = DeletedRecordsRepository();

  Future<List<SyncStatus>> _loadStatuses() async {
    final gtinCount = await _syncQueueRepository.pendingCountByDomain(SyncDomains.gtin);
    final carCount = await _syncQueueRepository.pendingCountByDomain(SyncDomains.car);
    final carMileageCount = await _syncQueueRepository.pendingCountByDomain(SyncDomains.carMileage);
    final androidCount = await _syncQueueRepository.pendingCountByDomain(SyncDomains.android);
    final wingetCount = Platform.isWindows
        ? await _syncQueueRepository.pendingCountByDomain(SyncDomains.winget)
        : 0;

    final statuses = [
      SyncStatus(
        domain: 'GTIN',
        state: gtinCount > 0 ? SyncState.syncing : SyncState.idle,
        pendingOperations: gtinCount,
      ),
      SyncStatus(
        domain: 'Voitures',
        state: carCount > 0 ? SyncState.syncing : SyncState.idle,
        pendingOperations: carCount,
      ),
      SyncStatus(
        domain: 'Kilometrage voitures',
        state: carMileageCount > 0 ? SyncState.syncing : SyncState.idle,
        pendingOperations: carMileageCount,
      ),
      SyncStatus(
        domain: 'Applications Android',
        state: androidCount > 0 ? SyncState.syncing : SyncState.idle,
        pendingOperations: androidCount,
      ),
    ];

    if (Platform.isWindows) {
      statuses.add(
        SyncStatus(
          domain: 'Applications Winget',
          state: wingetCount > 0 ? SyncState.syncing : SyncState.idle,
          pendingOperations: wingetCount,
        ),
      );
    }

    return statuses;
  }

  Future<void> _goToForm(Widget page) async {
    final created = await Navigator.of(context).push<bool>(
      MaterialPageRoute(builder: (_) => page),
    );

    if (!mounted || created != true) {
      return;
    }

    setState(() {
      _refreshCounter++;
    });
  }

  Future<void> _openFormForDomain(String domain) async {
    if (domain == 'GTIN') {
      await _goToForm(const GtinOfflineFormPage());
      return;
    }

    if (domain == 'Kilometrage voitures') {
      await _goToForm(const CarMileageOfflineFormPage());
      return;
    }

    if (domain == 'Voitures') {
      await _goToForm(const CarOfflineFormPage());
      return;
    }

    if (domain == 'Applications Winget') {
      await _goToForm(const WingetAppOfflineFormPage());
      return;
    }

    await _goToForm(const AndroidAppOfflineFormPage());
  }

  void _openHomeFromDrawer() {
    setState(() {
      _selectedMenuIndex = 0;
    });
    Navigator.of(context).pop();
  }

  void _openSyncListFromDrawer() {
    setState(() {
      _selectedMenuIndex = 1;
    });
    Navigator.of(context).pop();
  }

  void _openGatewaySettingsFromDrawer() {
    setState(() {
      _selectedMenuIndex = 2;
    });
    Navigator.of(context).pop();
  }

  void _openCrudPage(Widget page) {
    Navigator.of(context).pop();
    Navigator.of(context).push(MaterialPageRoute(builder: (_) => page));
  }

  void _openSyncListFromHome() {
    setState(() {
      _selectedMenuIndex = 1;
    });
  }

  Future<void> _syncNow() async {
    final result = await widget.onSyncNow();
    if (!mounted) {
      return;
    }

    final message = result.processed == 0
        ? 'Aucune operation a synchroniser.'
        : 'Sync terminee: ${result.succeeded} succes, ${result.failed} echec(s).';

    ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(message)));

    setState(() {
      _refreshCounter++;
    });
  }

  Future<void> _purgeDeletedRows() async {
    final deletedBeforePurge = await _deletedRecordsRepository.countDeletedRows();

    if (!mounted) {
      return;
    }

    if (deletedBeforePurge == 0) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Aucun element supprime a purger.')),
      );
      return;
    }

    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Purger les suppressions locales'),
        content: Text(
          '$deletedBeforePurge element(s) marques comme supprimes seront definitivement effaces du stockage local.',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(false),
            child: const Text('Annuler'),
          ),
          FilledButton(
            onPressed: () => Navigator.of(context).pop(true),
            child: const Text('Purger'),
          ),
        ],
      ),
    );

    if (confirmed != true) {
      return;
    }

    final purgedCount = await _deletedRecordsRepository.purgeDeletedRows();
    if (!mounted) {
      return;
    }

    await widget.onSyncNow();
    if (!mounted) {
      return;
    }

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('$purgedCount element(s) purges localement.')),
    );

    setState(() {
      _refreshCounter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    final showSyncActions = _selectedMenuIndex == 1;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Data Mobile'),
        actions: [
          if (showSyncActions) ...[
            IconButton(
              onPressed: _purgeDeletedRows,
              icon: const Icon(Icons.delete_sweep),
              tooltip: 'Purger les suppressions locales',
            ),
            IconButton(
              onPressed: _syncNow,
              icon: const Icon(Icons.sync),
              tooltip: 'Synchroniser maintenant',
            ),
          ],
        ],
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            const DrawerHeader(
              decoration: BoxDecoration(color: Colors.indigo),
              child: Align(
                alignment: Alignment.bottomLeft,
                child: Text(
                  'Data Mobile',
                  style: TextStyle(color: Colors.white, fontSize: 24, fontWeight: FontWeight.w600),
                ),
              ),
            ),
            ListTile(
              leading: const Icon(Icons.home_outlined),
              title: const Text('Accueil'),
              selected: _selectedMenuIndex == 0,
              onTap: _openHomeFromDrawer,
            ),
            ListTile(
              leading: const Icon(Icons.sync),
              title: const Text('Liste des synchronisations'),
              selected: _selectedMenuIndex == 1,
              onTap: _openSyncListFromDrawer,
            ),
            ListTile(
              leading: const Icon(Icons.settings_ethernet),
              title: const Text('Parametrage gateway'),
              selected: _selectedMenuIndex == 2,
              onTap: _openGatewaySettingsFromDrawer,
            ),
            const Divider(),
            const Padding(
              padding: EdgeInsets.symmetric(horizontal: 16, vertical: 4),
              child: Text('Donnees', style: TextStyle(fontSize: 12, color: Colors.grey)),
            ),
            ListTile(
              leading: const Icon(Icons.qr_code),
              title: const Text('GTINs'),
              onTap: () => _openCrudPage(const GtinListPage()),
            ),
            ListTile(
              leading: const Icon(Icons.directions_car),
              title: const Text('Voitures'),
              onTap: () => _openCrudPage(const CarListPage()),
            ),
            ListTile(
              leading: const Icon(Icons.android),
              title: const Text('Applications Android'),
              onTap: () => _openCrudPage(const AndroidAppListPage()),
            ),
            if (Platform.isWindows)
              ListTile(
                leading: const Icon(Icons.desktop_windows),
                title: const Text('Applications Winget'),
                onTap: () => _openCrudPage(const WingetAppListPage()),
              ),
            const Divider(),
            ListTile(
              leading: const Icon(Icons.bug_report_outlined),
              title: const Text('Support'),
              onTap: () async {
                final uri = Uri.parse('https://github.com/tiogars/data/issues/new');
                final launched = await launchUrl(uri, mode: LaunchMode.externalApplication);
                if (!launched && context.mounted) {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text("Impossible d'ouvrir le lien de support.")),
                  );
                }
              },
            ),
          ],
        ),
      ),
      body: _selectedMenuIndex == 0
          ? HomeMetricsPage(
              refreshCounter: _refreshCounter,
              onOpenSynchronizations: _openSyncListFromHome,
            )
          : _selectedMenuIndex == 1
              ? SynchronizationsPage(
                  refreshCounter: _refreshCounter,
                  statusLoader: _loadStatuses,
                  onSelectDomain: _openFormForDomain,
                )
              : GatewaySettingsPage(
                  initialConfig: widget.runtimeConfig,
                  onSave: widget.onSaveRuntimeConfig,
                ),
    );
  }
}
