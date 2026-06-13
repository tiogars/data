class CarItem {
  const CarItem({
    this.id,
    required this.name,
    this.plateNumber,
    required this.updatedAt,
    this.isDirty = false,
  });

  final int? id;
  final String name;
  final String? plateNumber;
  final DateTime updatedAt;
  final bool isDirty;

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'plate_number': plateNumber,
      'updated_at': updatedAt.toIso8601String(),
      'is_dirty': isDirty ? 1 : 0,
    };
  }

  static CarItem fromMap(Map<String, dynamic> map) {
    return CarItem(
      id: map['id'] as int?,
      name: map['name'] as String,
      plateNumber: map['plate_number'] as String?,
      updatedAt: DateTime.parse(map['updated_at'] as String),
      isDirty: (map['is_dirty'] as int? ?? 0) == 1,
    );
  }
}
