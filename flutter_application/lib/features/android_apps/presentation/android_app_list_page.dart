import 'package:flutter/material.dart';
import 'package:flutter_application/features/android_apps/data/android_app_local_repository.dart';
import 'package:flutter_application/features/android_apps/domain/android_app_item.dart';
import 'package:flutter_application/features/android_apps/presentation/android_app_offline_form_page.dart';

class AndroidAppListPage extends StatefulWidget {
  const AndroidAppListPage({super.key});

  @override
  State<AndroidAppListPage> createState() => _AndroidAppListPageState();
}

class _AndroidAppListPageState extends State<AndroidAppListPage> {
  static const AndroidAppLocalRepository _repository = AndroidAppLocalRepository();
  late Future<List<AndroidAppItem>> _itemsFuture;

  @override
  void initState() {
    super.initState();
    _reload();
  }

  void _reload() {
    setState(() {
      _itemsFuture = _repository.findAll();
    });
  }

  Future<void> _openForm({AndroidAppItem? item}) async {
    final saved = await Navigator.of(context).push<bool>(
      MaterialPageRoute(builder: (_) => AndroidAppOfflineFormPage(item: item)),
    );
    if (saved == true) _reload();
  }

  Future<void> _delete(AndroidAppItem item) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Supprimer l\'application'),
        content: Text('Supprimer "${item.name}" ?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(ctx).pop(false),
            child: const Text('Annuler'),
          ),
          FilledButton(
            onPressed: () => Navigator.of(ctx).pop(true),
            child: const Text('Supprimer'),
          ),
        ],
      ),
    );
    if (confirmed == true) {
      await _repository.deleteOffline(item);
      _reload();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Applications Android')),
      body: FutureBuilder<List<AndroidAppItem>>(
        future: _itemsFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) {
            return Center(child: Text('Erreur : ${snapshot.error}'));
          }
          final items = snapshot.data ?? [];
          if (items.isEmpty) {
            return const Center(child: Text('Aucune application Android enregistree.'));
          }
          return ListView.separated(
            itemCount: items.length,
            separatorBuilder: (_, _) => const Divider(height: 1),
            itemBuilder: (context, index) {
              final item = items[index];
              return ListTile(
                leading: Icon(
                  Icons.android,
                  color: item.isDirty ? Colors.orange : Colors.grey,
                ),
                title: Text(item.name),
                subtitle: Text(item.packageName),
                trailing: IconButton(
                  icon: const Icon(Icons.delete_outline),
                  onPressed: () => _delete(item),
                ),
                onTap: () => _openForm(item: item),
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _openForm(),
        tooltip: 'Nouvelle application Android',
        child: const Icon(Icons.add),
      ),
    );
  }
}
