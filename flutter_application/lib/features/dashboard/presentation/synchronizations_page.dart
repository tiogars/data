import 'package:flutter/material.dart';
import 'package:flutter_application/core/sync/sync_status.dart';

class SynchronizationsPage extends StatelessWidget {
  const SynchronizationsPage({
    super.key,
    required this.refreshCounter,
    required this.statusLoader,
    required this.onSelectDomain,
  });

  final int refreshCounter;
  final Future<List<SyncStatus>> Function() statusLoader;
  final Future<void> Function(String domain) onSelectDomain;

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<List<SyncStatus>>(
      key: ValueKey(refreshCounter),
      future: statusLoader(),
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
                  onSelectDomain(status.domain);
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
