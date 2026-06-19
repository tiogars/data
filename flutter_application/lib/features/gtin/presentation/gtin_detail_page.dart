import 'package:flutter/material.dart';
import 'package:flutter_application/features/gtin/data/gtin_local_repository.dart';
import 'package:flutter_application/features/gtin/domain/gtin_item.dart';
import 'package:flutter_application/features/gtin/presentation/gtin_offline_form_page.dart';
import 'package:intl/intl.dart';

class GtinDetailPage extends StatelessWidget {
  const GtinDetailPage({super.key, required this.item});

  final GtinItem item;

  static const GtinLocalRepository _repository = GtinLocalRepository();
  static final DateFormat _dateFormat = DateFormat('dd/MM/yyyy HH:mm');

  Future<void> _edit(BuildContext context) async {
    final saved = await Navigator.of(context).push<bool>(
      MaterialPageRoute(builder: (_) => GtinOfflineFormPage(item: item)),
    );
    if (saved == true && context.mounted) {
      Navigator.of(context).pop(true);
    }
  }

  Future<void> _delete(BuildContext context) async {
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
      if (context.mounted) {
        Navigator.of(context).pop(true);
      }
    }
  }

  Widget _buildAttribute({required String label, required String value}) {
    return Card(
      child: ListTile(
        title: Text(label),
        subtitle: Text(value),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Detail du GTIN')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          _buildAttribute(label: 'Code', value: item.code),
          _buildAttribute(
            label: 'Description',
            value: item.description.trim().isEmpty ? 'Non renseignee' : item.description,
          ),
          _buildAttribute(
            label: 'Derniere mise a jour',
            value: _dateFormat.format(item.updatedAt.toLocal()),
          ),
          _buildAttribute(
            label: 'Statut synchro',
            value: item.isDirty ? 'Synchronisation en attente' : 'Synchronise',
          ),
          const SizedBox(height: 16),
          FilledButton.icon(
            onPressed: () => _edit(context),
            icon: const Icon(Icons.edit_outlined),
            label: const Text('Modifier'),
          ),
          const SizedBox(height: 12),
          OutlinedButton.icon(
            onPressed: () => _delete(context),
            icon: const Icon(Icons.delete_outline),
            label: const Text('Supprimer'),
          ),
        ],
      ),
    );
  }
}
