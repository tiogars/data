import 'package:oidc/oidc.dart';
import 'package:shared_preferences/shared_preferences.dart';

/// Store OIDC persistant basé sur SharedPreferences.
///
/// Il remplace oidc_default_store afin d'éviter la dépendance
/// flutter_secure_storage_windows sur Windows.
class OidcSharedPreferencesStore implements OidcStore {
  OidcSharedPreferencesStore({this.storagePrefix = 'oidc'});

  final String storagePrefix;
  SharedPreferences? _prefs;

  SharedPreferences get _sharedPreferences => _prefs!;

  String _prefixFor(OidcStoreNamespace namespace, String? managerId) {
    return [storagePrefix, managerId, namespace.value]
        .nonNulls
        .join('.');
  }

  String _keyFor(
    OidcStoreNamespace namespace,
    String key,
    String? managerId,
  ) {
    return [storagePrefix, managerId, namespace.value, key].nonNulls.join('.');
  }

  @override
  Future<void> init() async {
    _prefs ??= await SharedPreferences.getInstance();
  }

  @override
  Future<Set<String>> getAllKeys(
    OidcStoreNamespace namespace, {
    String? managerId,
  }) async {
    await init();
    final prefix = '${_prefixFor(namespace, managerId)}.';
    final keys = _sharedPreferences
        .getKeys()
        .where((key) => key.startsWith(prefix))
        .map((key) => key.substring(prefix.length))
        .toSet();
    return keys;
  }

  @override
  Future<Map<String, String>> getMany(
    OidcStoreNamespace namespace, {
    required Set<String> keys,
    String? managerId,
  }) async {
    await init();
    final values = <String, String>{};
    for (final key in keys) {
      final value = _sharedPreferences.getString(
        _keyFor(namespace, key, managerId),
      );
      if (value != null) {
        values[key] = value;
      }
    }
    return values;
  }

  @override
  Future<void> removeMany(
    OidcStoreNamespace namespace, {
    required Set<String> keys,
    String? managerId,
  }) async {
    await init();
    for (final key in keys) {
      await _sharedPreferences.remove(_keyFor(namespace, key, managerId));
    }
  }

  @override
  Future<void> setMany(
    OidcStoreNamespace namespace, {
    required Map<String, String> values,
    String? managerId,
  }) async {
    await init();
    for (final entry in values.entries) {
      await _sharedPreferences.setString(
        _keyFor(namespace, entry.key, managerId),
        entry.value,
      );
    }
  }
}
