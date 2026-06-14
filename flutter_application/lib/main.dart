import 'dart:io';

import 'package:flutter/widgets.dart';
import 'package:flutter_application/app/app.dart';
import 'package:sqflite_common_ffi/sqflite_ffi.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();

  if (Platform.isWindows) {
    sqfliteFfiInit();
    databaseFactory = databaseFactoryFfi;
  }

  runApp(const DataMobileApp());
}
