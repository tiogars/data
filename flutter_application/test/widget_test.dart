import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:flutter_application/app/app.dart';

void main() {
  testWidgets('shouldRenderLoadingStateWhenAppStarts', (WidgetTester tester) async {
    await tester.pumpWidget(const DataMobileApp());

    expect(find.byType(MaterialApp), findsOneWidget);
    expect(find.byType(CircularProgressIndicator), findsOneWidget);
  });
}
