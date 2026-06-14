import 'package:flutter/material.dart';
import 'package:flutter_application/features/gtin/data/gtin_local_repository.dart';
import 'package:flutter_application/features/gtin/domain/gtin_item.dart';
import 'package:flutter_application/features/gtin/presentation/gtin_offline_form_page.dart';

class GtinListPage extends StatefulWidget {
  const GtinListPage({super.key});

  @override
  State<GtinListPage> createState() => _GtinListPageState();
}

class _GtinListPageState extends State<GtinListPage> {
  static const GtinLocalRepository _repository = GtinLocalRepository();
  late Future<List<GtinItem>> _itemsFuture;

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

  Future<void> _openForm({GtinItem? item}) async {
    final saved = await Navigator.of(context).push<bool>(
      MaterialPageRoute(builder: (_) => GtinOfflineFormPage(item: item)),
    );
    if (saved == true) _reload();
  }

  Future<void> _delete(GtinItem item) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Supprimer le GTIN'),
        content: Text('Supprimer "${item.code}" ?'),
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
      appBar: AppBar(title: const Text('GTINs')),
      body: FutureBuilder<List<GtinItem>>(
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
            return const Center(child: Text('Aucun GTIN enregistre.'));
          }
          return ListView.separated(
            itemCount: items.length,
            separatorBuilder: (_, __) => const Divider(height: 1),
            itemBuilder: (context, index) {
              final item = items[index];
              return ListTile(
                leading: Icon(
                  Icons.qr_code,
                  color: item.isDirty ? Colors.orange : Colors.grey,
                ),
                title: Text(item.code),
                subtitle: item.description.isNotEmpty ? Text(item.description) : null,
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
        tooltip: 'Nouveau GTIN',
        child: const Icon(Icons.add),
      ),
    );
  }
}
