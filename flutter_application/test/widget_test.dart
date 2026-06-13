import 'package:flutter_test/flutter_test.dart';

import 'package:flutter_application/app/app.dart';

void main() {
  testWidgets('shouldRenderMobileDashboardWhenAppStarts', (WidgetTester tester) async {
    await tester.pumpWidget(const DataMobileApp());

    expect(find.text('Data Mobile'), findsOneWidget);
    expect(find.text('GTIN'), findsOneWidget);
    expect(find.text('Kilometrage voitures'), findsOneWidget);
    expect(find.text('Applications Android'), findsOneWidget);
  });
}
