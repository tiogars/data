import 'package:flutter/material.dart';
import 'package:url_launcher/link.dart';
import 'package:flutter_application/core/database/database_provider.dart';
import 'package:flutter_application/core/database/deleted_records_repository.dart';
import 'package:flutter_application/core/database/sync_queue_repository.dart';
import 'package:flutter_application/core/database/table_names.dart';
import 'package:flutter_application/core/sync/sync_domains.dart';
import 'package:flutter_application/features/android_apps/data/android_app_local_repository.dart';
import 'package:flutter_application/features/gtin/data/gtin_local_repository.dart';
import 'package:flutter_application/features/vehicles/data/car_local_repository.dart';

class HomeMetricsPage extends StatelessWidget {
  const HomeMetricsPage({
    super.key,
    required this.refreshCounter,
    required this.onOpenSynchronizations,
  });

  final int refreshCounter;
  final VoidCallback onOpenSynchronizations;

  static const GtinLocalRepository _gtinRepository = GtinLocalRepository();
  static const CarLocalRepository _carRepository = CarLocalRepository();
  static const AndroidAppLocalRepository _androidRepository = AndroidAppLocalRepository();
  static const SyncQueueRepository _syncQueueRepository = SyncQueueRepository();
  static const DeletedRecordsRepository _deletedRecordsRepository = DeletedRecordsRepository();
  static final Uri _documentationUri = Uri.parse('https://docs.data.tiogars.fr');

  Future<_HomeMetricsData> _loadMetrics() async {
    final results = await Future.wait<dynamic>([
      _gtinRepository.findAll(),
      _carRepository.findAll(),
      _androidRepository.findAll(),
      _countCarMileage(),
      _syncQueueRepository.pendingCountByDomain(SyncDomains.gtin),
      _syncQueueRepository.pendingCountByDomain(SyncDomains.car),
      _syncQueueRepository.pendingCountByDomain(SyncDomains.carMileage),
      _syncQueueRepository.pendingCountByDomain(SyncDomains.android),
      _syncQueueRepository.pendingCount(),
      _deletedRecordsRepository.countDeletedRows(),
    ]);

    final entries = [
      _MetricEntry(
        title: 'GTIN',
        total: (results[0] as List).length,
        pendingSync: results[4] as int,
        icon: Icons.qr_code,
      ),
      _MetricEntry(
        title: 'Voitures',
        total: (results[1] as List).length,
        pendingSync: results[5] as int,
        icon: Icons.directions_car,
      ),
      _MetricEntry(
        title: 'Kilometrages',
        total: results[3] as int,
        pendingSync: results[6] as int,
        icon: Icons.speed,
      ),
      _MetricEntry(
        title: 'Apps Android',
        total: (results[2] as List).length,
        pendingSync: results[7] as int,
        icon: Icons.android,
      ),
    ];

    return _HomeMetricsData(
      entries: entries,
      totalPendingSync: results[8] as int,
      deletedRows: results[9] as int,
    );
  }

  Future<int> _countCarMileage() async {
    final db = await DatabaseProvider.instance.database;
    final result = await db.rawQuery(
      'SELECT COUNT(*) as count FROM ${TableNames.carMileage} WHERE deleted_at IS NULL',
    );

    return (result.first['count'] as int?) ?? 0;
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<_HomeMetricsData>(
      key: ValueKey(refreshCounter),
      future: _loadMetrics(),
      builder: (context, snapshot) {
        final data = snapshot.data;

        if (snapshot.connectionState == ConnectionState.waiting && data == null) {
          return const Center(child: CircularProgressIndicator());
        }

        if (snapshot.hasError && data == null) {
          return Center(
            child: Padding(
              padding: const EdgeInsets.all(24),
              child: Text(
                'Impossible de charger les metriques locales.',
                textAlign: TextAlign.center,
                style: Theme.of(context).textTheme.bodyLarge,
              ),
            ),
          );
        }

        final metrics = data ??
            const _HomeMetricsData(
              entries: [
                _MetricEntry(title: 'GTIN', total: 0, pendingSync: 0, icon: Icons.qr_code),
                _MetricEntry(title: 'Voitures', total: 0, pendingSync: 0, icon: Icons.directions_car),
                _MetricEntry(title: 'Kilometrages', total: 0, pendingSync: 0, icon: Icons.speed),
                _MetricEntry(title: 'Apps Android', total: 0, pendingSync: 0, icon: Icons.android),
              ],
              totalPendingSync: 0,
              deletedRows: 0,
            );

        return ListView(
          padding: const EdgeInsets.all(16),
          children: [
            Text(
              'Accueil',
              style: Theme.of(context).textTheme.headlineSmall,
            ),
            const SizedBox(height: 8),
            Text(
              'Vue synthese des donnees locales et de la synchronisation.',
              style: Theme.of(context).textTheme.bodyMedium,
            ),
            const SizedBox(height: 16),
            Link(
              uri: _documentationUri,
              target: LinkTarget.blank,
              builder: (context, followLink) => Align(
                alignment: Alignment.centerLeft,
                child: FilledButton.icon(
                  onPressed: followLink,
                  icon: const Icon(Icons.menu_book_outlined),
                  label: const Text('Documentation'),
                ),
              ),
            ),
            const SizedBox(height: 16),
            Wrap(
              spacing: 12,
              runSpacing: 12,
              children: metrics.entries
                  .map(
                    (entry) => SizedBox(
                      width: MediaQuery.of(context).size.width > 700
                          ? (MediaQuery.of(context).size.width - 56) / 2
                          : double.infinity,
                      child: Card(
                        child: Padding(
                          padding: const EdgeInsets.all(16),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Row(
                                children: [
                                  Icon(entry.icon),
                                  const SizedBox(width: 8),
                                  Expanded(
                                    child: Text(
                                      entry.title,
                                      style: Theme.of(context).textTheme.titleMedium,
                                    ),
                                  ),
                                ],
                              ),
                              const SizedBox(height: 12),
                              Text(
                                '${entry.total}',
                                style: Theme.of(context).textTheme.headlineSmall,
                              ),
                              const SizedBox(height: 4),
                              Text('enregistrements locaux'),
                              const SizedBox(height: 8),
                              Text('${entry.pendingSync} operation(s) en attente'),
                            ],
                          ),
                        ),
                      ),
                    ),
                  )
                  .toList(),
            ),
            const SizedBox(height: 12),
            Card(
              child: ListTile(
                leading: const Icon(Icons.sync_problem),
                title: Text('File de sync: ${metrics.totalPendingSync} operation(s)'),
                subtitle: Text('${metrics.deletedRows} element(s) supprimes en attente de purge'),
                trailing: const Icon(Icons.arrow_forward),
                onTap: onOpenSynchronizations,
              ),
            ),
          ],
        );
      },
    );
  }
}

class _HomeMetricsData {
  const _HomeMetricsData({
    required this.entries,
    required this.totalPendingSync,
    required this.deletedRows,
  });

  final List<_MetricEntry> entries;
  final int totalPendingSync;
  final int deletedRows;
}

class _MetricEntry {
  const _MetricEntry({
    required this.title,
    required this.total,
    required this.pendingSync,
    required this.icon,
  });

  final String title;
  final int total;
  final int pendingSync;
  final IconData icon;
}
