import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_application/features/vehicles/domain/car_item.dart';
import 'package:flutter_application/features/vehicles/presentation/car_detail_page.dart';

void main() {
  testWidgets('shouldDisplayCarAttributesAndActionsWhenDetailPageIsOpened', (WidgetTester tester) async {
    final item = CarItem(
      id: 10,
      remoteId: 'remote-1',
      name: 'Peugeot 208',
      plateNumber: 'AB-123-CD',
      updatedAt: DateTime(2026, 6, 18, 9),
      isDirty: false,
    );

    await tester.pumpWidget(
      MaterialApp(
        home: CarDetailPage(item: item),
      ),
    );

    expect(find.text('Detail de la voiture'), findsOneWidget);
    expect(find.text('Nom'), findsOneWidget);
    expect(find.text('Peugeot 208'), findsOneWidget);
    expect(find.text('Immatriculation'), findsOneWidget);
    expect(find.text('AB-123-CD'), findsOneWidget);
    expect(find.text('Statut synchro'), findsOneWidget);
    expect(find.text('Synchronise'), findsOneWidget);
    expect(find.widgetWithText(FilledButton, 'Modifier'), findsOneWidget);
    expect(find.widgetWithText(OutlinedButton, 'Voir les kilometrages'), findsOneWidget);
    expect(find.widgetWithText(OutlinedButton, 'Supprimer'), findsOneWidget);
  });

  testWidgets('shouldDisplayNonRenseigneeWhenCarPlateIsEmpty', (WidgetTester tester) async {
    final item = CarItem(
      id: 11,
      name: 'Voiture sans plaque',
      updatedAt: DateTime(2026, 6, 18, 9),
      isDirty: true,
    );

    await tester.pumpWidget(
      MaterialApp(
        home: CarDetailPage(item: item),
      ),
    );

    expect(find.text('Non renseignee'), findsOneWidget);
    expect(find.text('Synchronisation en attente'), findsOneWidget);
  });
}
