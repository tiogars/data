class SyncOperation {
  const SyncOperation({
    this.id,
    required this.domain,
    required this.operationType,
    this.entityId,
    required this.payload,
    required this.createdAt,
    this.attempts = 0,
  });

  final int? id;
  final String domain;
  final String operationType;
  final int? entityId;
  final Map<String, dynamic> payload;
  final DateTime createdAt;
  final int attempts;
}
