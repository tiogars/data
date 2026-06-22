import 'package:dio/dio.dart';

import 'package:flutter_application/core/api/mobile_runtime_config.dart';

class CursorSyncBatch {
  const CursorSyncBatch({
    required this.items,
    this.nextCursor,
    this.deletedIds = const <String>[],
  });

  final List<Map<String, dynamic>> items;
  final String? nextCursor;
  final List<String> deletedIds;
}

class GatewayApiClient {
  GatewayApiClient({required MobileRuntimeConfig config})
      : _dio = Dio(
          BaseOptions(
            baseUrl: config.gatewayBaseUrl,
            connectTimeout: const Duration(seconds: 10),
            receiveTimeout: const Duration(seconds: 15),
            headers: {
              if (config.jwtToken != null) 'Authorization': 'Bearer ${config.jwtToken}',
              'Content-Type': 'application/json',
            },
          ),
        );

  final Dio _dio;

  Future<List<Map<String, dynamic>>> fetchGtinItems() async {
    final response = await _dio.get('/gtin/list');
    return _extractItems(response.data);
  }

  Future<CursorSyncBatch?> fetchGtinItemsIncremental({String? cursor}) async {
    final response = await _dio.get(
      '/api/v1/sync/gtin/changes',
      queryParameters: _cursorQuery(cursor),
    );
    return _extractCursorBatchIfPresent(response.data);
  }

  Future<List<Map<String, dynamic>>> fetchCarItems() async {
    final response = await _dio.get('/car/list');
    return _extractItems(response.data);
  }

  Future<CursorSyncBatch?> fetchCarItemsIncremental({String? cursor}) async {
    final response = await _dio.get(
      '/api/v1/sync/car/changes',
      queryParameters: _cursorQuery(cursor),
    );
    return _extractCursorBatchIfPresent(response.data);
  }

  Future<List<Map<String, dynamic>>> fetchAndroidItems() async {
    final response = await _dio.get('/android/list');
    return _extractItems(response.data);
  }

  Future<CursorSyncBatch?> fetchAndroidItemsIncremental({String? cursor}) async {
    final response = await _dio.get(
      '/api/v1/sync/android/changes',
      queryParameters: _cursorQuery(cursor),
    );
    return _extractCursorBatchIfPresent(response.data);
  }

  Future<List<Map<String, dynamic>>> fetchWingetItems() async {
    final response = await _dio.get('/winget/list');
    return _extractItems(response.data);
  }

  Future<CursorSyncBatch?> fetchWingetItemsIncremental({String? cursor}) async {
    final response = await _dio.get(
      '/api/v1/sync/winget/changes',
      queryParameters: _cursorQuery(cursor),
    );
    return _extractCursorBatchIfPresent(response.data);
  }

  Future<List<Map<String, dynamic>>> fetchCarMileageItems({int pageSize = 100}) async {
    final collected = <Map<String, dynamic>>[];
    var page = 0;

    while (true) {
      final response = await _dio.get(
        '/car-mileage/search',
        queryParameters: {
          'page': page,
          'size': pageSize,
        },
      );

      final pageItems = _extractItems(response.data);
      collected.addAll(pageItems);

      if (pageItems.length < pageSize) {
        return collected;
      }

      page++;
    }
  }

  Future<CursorSyncBatch?> fetchCarMileageItemsIncremental({String? cursor, int pageSize = 100}) async {
    final response = await _dio.get(
      '/api/v1/sync/car-mileage/changes',
      queryParameters: {
        ..._cursorQuery(cursor),
        'size': pageSize,
      },
    );
    return _extractCursorBatchIfPresent(response.data);
  }

  Future<void> postDomainOperation({
    required String domain,
    required String operationType,
    required Map<String, dynamic> payload,
    int? entityId,
  }) async {
    final path = entityId == null ? '/$domain' : '/$domain/$entityId';

    switch (operationType) {
      case 'create':
        await _dio.post(path, data: payload);
        return;
      case 'update':
        await _dio.put(path, data: payload);
        return;
      case 'delete':
        await _dio.delete(path);
        return;
      default:
        await _dio.post(path, data: payload);
    }
  }

  List<Map<String, dynamic>> _extractItems(dynamic data) {
    if (data is! Map<String, dynamic>) {
      return const <Map<String, dynamic>>[];
    }

    final items = data['items'];
    if (items is! List) {
      return const <Map<String, dynamic>>[];
    }

    return items.whereType<Map>().map((item) => Map<String, dynamic>.from(item)).toList();
  }

  Map<String, dynamic> _cursorQuery(String? cursor) {
    if (cursor == null || cursor.isEmpty) {
      return const <String, dynamic>{};
    }

    return <String, dynamic>{
      'cursor': cursor,
      'updatedAfter': cursor,
    };
  }

  CursorSyncBatch? _extractCursorBatchIfPresent(dynamic data) {
    if (data is! Map<String, dynamic>) {
      return null;
    }

    final hasCursorMarker = data.containsKey('nextCursor') || data.containsKey('hasMore');

    if (!hasCursorMarker) {
      return null;
    }

    final items = _extractItems(data);
    final nextCursor = data['nextCursor'] as String?;

    final rawDeletedIds = data['deletedIds'];
    final deletedIds = rawDeletedIds is List
        ? rawDeletedIds.map((value) => value.toString()).where((value) => value.isNotEmpty).toList()
        : const <String>[];

    return CursorSyncBatch(
      items: items,
      nextCursor: nextCursor,
      deletedIds: deletedIds,
    );
  }
}
