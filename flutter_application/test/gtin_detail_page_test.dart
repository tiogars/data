import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_application/features/gtin/domain/gtin_item.dart';
import 'package:flutter_application/features/gtin/presentation/gtin_detail_page.dart';

void main() {
  testWidgets('shouldDisplayGtinAttributesAndActionsWhenDetailPageIsOpened', (WidgetTester tester) async {
    final item = GtinItem(
      id: 1,
      code: '3760168350015',
      description: 'Produit test',
      updatedAt: DateTime(2026, 6, 18, 10),
      isDirty: false,
    );

    await tester.pumpWidget(
      MaterialApp(
        home: GtinDetailPage(item: item),
      ),
    );

    expect(find.text('Detail du GTIN'), findsOneWidget);
    expect(find.text('Code'), findsOneWidget);
    expect(find.text('3760168350015'), findsOneWidget);
    expect(find.text('Description'), findsOneWidget);
    expect(find.text('Produit test'), findsOneWidget);
    expect(find.text('Statut synchro'), findsOneWidget);
    expect(find.text('Synchronise'), findsOneWidget);
    expect(find.widgetWithText(FilledButton, 'Modifier'), findsOneWidget);
    expect(find.widgetWithText(OutlinedButton, 'Supprimer'), findsOneWidget);
  });

  testWidgets('shouldDisplayNonRenseigneeWhenGtinDescriptionIsEmpty', (WidgetTester tester) async {
    final item = GtinItem(
      id: 2,
      code: '0000000000000',
      description: '',
      updatedAt: DateTime(2026, 6, 18, 10),
      isDirty: true,
    );

    await tester.pumpWidget(
      MaterialApp(
        home: GtinDetailPage(item: item),
      ),
    );

    expect(find.text('Non renseignee'), findsOneWidget);
    expect(find.text('Synchronisation en attente'), findsOneWidget);
  });
}
