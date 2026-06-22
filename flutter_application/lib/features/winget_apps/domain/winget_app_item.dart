class WingetAppItem {
  const WingetAppItem({
    this.id,
    required this.name,
    this.description,
    required this.wingetId,
    required this.installCommand,
    this.tags,
    required this.updatedAt,
    this.isDirty = false,
  });

  final int? id;
  final String name;
  final String? description;
  final String wingetId;
  final String installCommand;
  final String? tags;
  final DateTime updatedAt;
  final bool isDirty;

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'description': description,
      'winget_id': wingetId,
      'install_command': installCommand,
      'tags': tags,
      'updated_at': updatedAt.toIso8601String(),
      'is_dirty': isDirty ? 1 : 0,
    };
  }

  static WingetAppItem fromMap(Map<String, dynamic> map) {
    return WingetAppItem(
      id: map['id'] as int?,
      name: map['name'] as String,
      description: map['description'] as String?,
      wingetId: map['winget_id'] as String,
      installCommand: map['install_command'] as String,
      tags: map['tags'] as String?,
      updatedAt: DateTime.parse(map['updated_at'] as String),
      isDirty: (map['is_dirty'] as int? ?? 0) == 1,
    );
  }
}
