import 'package:flutter/material.dart';
import 'package:flutter_application/core/database/deleted_records_repository.dart';
import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/sync/sync_domains.dart';
import 'package:flutter_application/core/sync/sync_process_result.dart';
import 'package:flutter_application/core/sync/sync_status.dart';
import 'package:flutter_application/features/android_apps/presentation/android_app_offline_form_page.dart';
import 'package:flutter_application/features/gtin/presentation/gtin_offline_form_page.dart';
import 'package:flutter_application/features/vehicles/presentation/car_offline_form_page.dart';
import 'package:flutter_application/features/vehicles/presentation/car_mileage_offline_form_page.dart';

class DashboardPage extends StatefulWidget {
  const DashboardPage({super.key, required this.onSyncNow});

  final Future<SyncProcessResult> Function() onSyncNow;

  @override
  State<DashboardPage> createState() => _DashboardPageState();
}

class _DashboardPageState extends State<DashboardPage> {
  int _refreshCounter = 0;

  static const SyncQueueRepository _syncQueueRepository = SyncQueueRepository();
  static const DeletedRecordsRepository _deletedRecordsRepository = DeletedRecordsRepository();

  Future<List<SyncStatus>> _loadStatuses() async {
    final gtinCount = await _syncQueueRepository.pendingCountByDomain(SyncDomains.gtin);
    final carCount = await _syncQueueRepository.pendingCountByDomain(SyncDomains.car);
    final carMileageCount = await _syncQueueRepository.pendingCountByDomain(SyncDomains.carMileage);
    final androidCount = await _syncQueueRepository.pendingCountByDomain(SyncDomains.android);

    return [
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

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('$purgedCount element(s) purges localement.')),
    );

    setState(() {
      _refreshCounter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Data Mobile'),
        actions: [
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
      ),
      body: FutureBuilder<List<SyncStatus>>(
        key: ValueKey(_refreshCounter),
        future: _loadStatuses(),
        builder: (context, snapshot) {
          final statuses = snapshot.data ??
              const <SyncStatus>[
                SyncStatus(domain: 'GTIN', state: SyncState.idle),
                SyncStatus(domain: 'Voitures', state: SyncState.idle),
                SyncStatus(domain: 'Kilometrage voitures', state: SyncState.idle),
                SyncStatus(domain: 'Applications Android', state: SyncState.idle),
              ];

          return ListView.separated(
            padding: const EdgeInsets.all(16),
            itemCount: statuses.length,
            separatorBuilder: (context, index) => const SizedBox(height: 12),
            itemBuilder: (context, index) {
              final status = statuses[index];
              return Card(
                child: ListTile(
                  leading: const Icon(Icons.sync),
                  title: Text(status.domain),
                  subtitle: Text(_stateLabel(status.state)),
                  onTap: () {
                    if (status.domain == 'GTIN') {
                      _goToForm(const GtinOfflineFormPage());
                      return;
                    }

                    if (status.domain == 'Kilometrage voitures') {
                      _goToForm(const CarMileageOfflineFormPage());
                      return;
                    }

                    if (status.domain == 'Voitures') {
                      _goToForm(const CarOfflineFormPage());
                      return;
                    }

                    _goToForm(const AndroidAppOfflineFormPage());
                  },
                  trailing: status.pendingOperations > 0
                      ? CircleAvatar(
                          radius: 12,
                          child: Text(status.pendingOperations.toString()),
                        )
                      : const Icon(Icons.check_circle, color: Colors.green),
                ),
              );
            },
          );
        },
      ),
    );
  }

  String _stateLabel(SyncState state) {
    switch (state) {
      case SyncState.idle:
        return 'Synchronisation a jour';
      case SyncState.syncing:
        return 'Synchronisation en cours';
      case SyncState.error:
        return 'Erreur de synchronisation';
    }
  }
}
