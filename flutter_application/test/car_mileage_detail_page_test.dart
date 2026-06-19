import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_application/features/vehicles/domain/car_mileage_entry.dart';
import 'package:flutter_application/features/vehicles/presentation/car_mileage_detail_page.dart';

void main() {
  testWidgets('shouldDisplayCarMileageAttributesAndActionsWhenDetailPageIsOpened', (WidgetTester tester) async {
    const entry = CarMileageEntry(
      id: 5,
      carId: 'remote-car-1',
      readingAt: DateTime(2026, 6, 15, 8),
      odometerKm: 42500,
      fuelVolumeLiters: 35.5,
      fullTank: true,
      updatedAt: DateTime(2026, 6, 15, 8, 5),
      isDirty: false,
    );

    await tester.pumpWidget(
      const MaterialApp(
        home: CarMileageDetailPage(entry: entry),
      ),
    );

    expect(find.text('Detail du kilometrage'), findsOneWidget);
    expect(find.text('Date de releve'), findsOneWidget);
    expect(find.text('Kilometrage'), findsOneWidget);
    expect(find.text('42500 km'), findsOneWidget);
    expect(find.text('Volume carburant'), findsOneWidget);
    expect(find.text('35.5 L'), findsOneWidget);
    expect(find.text('Plein complet'), findsOneWidget);
    expect(find.text('Oui'), findsOneWidget);
    expect(find.text('Statut synchro'), findsOneWidget);
    expect(find.text('Synchronise'), findsOneWidget);
    expect(find.widgetWithText(FilledButton, 'Modifier'), findsOneWidget);
    expect(find.widgetWithText(OutlinedButton, 'Supprimer'), findsOneWidget);
  });

  testWidgets('shouldDisplayNonRenseigneeWhenFuelVolumeIsNull', (WidgetTester tester) async {
    const entry = CarMileageEntry(
      id: 6,
      carId: 'remote-car-2',
      readingAt: DateTime(2026, 6, 10, 7),
      odometerKm: 10000,
      updatedAt: DateTime(2026, 6, 10, 7, 1),
      isDirty: true,
    );

    await tester.pumpWidget(
      const MaterialApp(
        home: CarMileageDetailPage(entry: entry),
      ),
    );

    expect(find.text('Non renseigne'), findsOneWidget);
    expect(find.text('Synchronisation en attente'), findsOneWidget);
  });
}
