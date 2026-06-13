class GtinItem {
  const GtinItem({
    this.id,
    required this.code,
    required this.description,
    required this.updatedAt,
    this.isDirty = false,
  });

  final int? id;
  final String code;
  final String description;
  final DateTime updatedAt;
  final bool isDirty;

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'code': code,
      'description': description,
      'updated_at': updatedAt.toIso8601String(),
      'is_dirty': isDirty ? 1 : 0,
    };
  }

  static GtinItem fromMap(Map<String, dynamic> map) {
    return GtinItem(
      id: map['id'] as int?,
      code: map['code'] as String,
      description: map['description'] as String? ?? '',
      updatedAt: DateTime.parse(map['updated_at'] as String),
      isDirty: (map['is_dirty'] as int? ?? 0) == 1,
    );
  }
}
