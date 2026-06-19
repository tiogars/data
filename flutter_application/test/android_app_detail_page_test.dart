import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_application/features/android_apps/domain/android_app_item.dart';
import 'package:flutter_application/features/android_apps/presentation/android_app_detail_page.dart';
import 'package:url_launcher/link.dart';

void main() {
  testWidgets('shouldDisplayAndroidAppAttributesAndActionsWhenDetailPageIsOpened', (WidgetTester tester) async {
    const item = AndroidAppItem(
      id: 42,
      name: 'Data Mobile',
      packageName: 'fr.tiogars.data.mobile',
      category: null,
      description: null,
      updatedAt: DateTime(2026, 6, 18, 12),
      isDirty: true,
    );

    await tester.pumpWidget(
      const MaterialApp(
        home: AndroidAppDetailPage(item: item),
      ),
    );

    expect(find.text('Detail de l\'application'), findsOneWidget);
    expect(find.text('Nom'), findsOneWidget);
    expect(find.text('Data Mobile'), findsOneWidget);
    expect(find.text('Package'), findsOneWidget);
    expect(find.text('fr.tiogars.data.mobile'), findsOneWidget);
    expect(find.text('Categorie'), findsOneWidget);
    expect(find.text('Non renseignee'), findsNWidgets(2));
    expect(find.text('Statut synchro'), findsOneWidget);
    expect(find.text('Synchronisation en attente'), findsOneWidget);
    expect(find.widgetWithText(FilledButton, 'Modifier'), findsOneWidget);
    expect(find.widgetWithText(OutlinedButton, 'Ouvrir sur le Play Store'), findsOneWidget);
    expect(find.byType(Link), findsOneWidget);
    expect(find.widgetWithText(OutlinedButton, 'Supprimer'), findsOneWidget);
  });

  testWidgets('shouldHidePlayStoreLinkWhenPackageNameIsBlank', (WidgetTester tester) async {
    const item = AndroidAppItem(
      id: 42,
      name: 'Data Mobile',
      packageName: '   ',
      category: null,
      description: null,
      updatedAt: DateTime(2026, 6, 18, 12),
      isDirty: false,
    );

    await tester.pumpWidget(
      const MaterialApp(
        home: AndroidAppDetailPage(item: item),
      ),
    );

    expect(find.widgetWithText(OutlinedButton, 'Ouvrir sur le Play Store'), findsNothing);
    expect(find.byType(Link), findsNothing);
  });
}
