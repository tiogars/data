enum SyncState {
  idle,
  syncing,
  error,
}

class SyncStatus {
  const SyncStatus({
    required this.domain,
    required this.state,
    this.pendingOperations = 0,
  });

  final String domain;
  final SyncState state;
  final int pendingOperations;
}
