class CarMileageEntry {
  const CarMileageEntry({
    this.id,
    required this.carId,
    required this.readingAt,
    required this.odometerKm,
    this.fuelVolumeLiters,
    this.fullTank = false,
    required this.updatedAt,
    this.isDirty = false,
  });

  final int? id;
  final String carId;
  final DateTime readingAt;
  final int odometerKm;
  final double? fuelVolumeLiters;
  final bool fullTank;
  final DateTime updatedAt;
  final bool isDirty;

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'car_id': carId,
      'reading_at': readingAt.toIso8601String(),
      'odometer_km': odometerKm,
      'fuel_volume_liters': fuelVolumeLiters,
      'full_tank': fullTank ? 1 : 0,
      'updated_at': updatedAt.toIso8601String(),
      'is_dirty': isDirty ? 1 : 0,
    };
  }

  static CarMileageEntry fromMap(Map<String, dynamic> map) {
    return CarMileageEntry(
      id: map['id'] as int?,
      carId: map['car_id'] as String,
      readingAt: DateTime.parse(map['reading_at'] as String),
      odometerKm: (map['odometer_km'] as num).toInt(),
      fuelVolumeLiters: (map['fuel_volume_liters'] as num?)?.toDouble(),
      fullTank: (map['full_tank'] as int? ?? 0) == 1,
      updatedAt: DateTime.parse(map['updated_at'] as String),
      isDirty: (map['is_dirty'] as int? ?? 0) == 1,
    );
  }
}
