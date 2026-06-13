class AndroidAppItem {
  const AndroidAppItem({
    this.id,
    required this.name,
    required this.packageName,
    this.category,
    this.description,
    required this.updatedAt,
    this.isDirty = false,
  });

  final int? id;
  final String name;
  final String packageName;
  final String? category;
  final String? description;
  final DateTime updatedAt;
  final bool isDirty;

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'package_name': packageName,
      'category': category,
      'description': description,
      'updated_at': updatedAt.toIso8601String(),
      'is_dirty': isDirty ? 1 : 0,
    };
  }

  static AndroidAppItem fromMap(Map<String, dynamic> map) {
    return AndroidAppItem(
      id: map['id'] as int?,
      name: map['name'] as String,
      packageName: map['package_name'] as String,
      category: map['category'] as String?,
      description: map['description'] as String?,
      updatedAt: DateTime.parse(map['updated_at'] as String),
      isDirty: (map['is_dirty'] as int? ?? 0) == 1,
    );
  }
}
