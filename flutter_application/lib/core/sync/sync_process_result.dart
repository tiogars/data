class SyncProcessResult {
  const SyncProcessResult({
    required this.processed,
    required this.succeeded,
    required this.failed,
  });

  final int processed;
  final int succeeded;
  final int failed;
}