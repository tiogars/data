import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_application/features/winget_apps/data/winget_app_local_repository.dart';
import 'package:flutter_application/features/winget_apps/domain/winget_app_item.dart';
import 'package:flutter_application/features/winget_apps/presentation/winget_app_offline_form_page.dart';
import 'package:intl/intl.dart';

class WingetAppDetailPage extends StatelessWidget {
  const WingetAppDetailPage({super.key, required this.item});

  final WingetAppItem item;

  static const WingetAppLocalRepository _repository = WingetAppLocalRepository();
  static final DateFormat _dateFormat = DateFormat('dd/MM/yyyy HH:mm');

  Future<void> _edit(BuildContext context) async {
    final saved = await Navigator.of(context).push<bool>(
      MaterialPageRoute(builder: (_) => WingetAppOfflineFormPage(item: item)),
    );
    if (saved == true && context.mounted) {
      Navigator.of(context).pop(true);
    }
  }

  Future<void> _delete(BuildContext context) async {
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
      if (context.mounted) {
        Navigator.of(context).pop(true);
      }
    }
  }

  Widget _buildAttribute({
    required String label,
    required String value,
  }) {
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
      appBar: AppBar(title: const Text('Detail de l\'application Winget')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          _buildAttribute(
            label: 'Nom',
            value: item.name,
          ),
          _buildAttribute(
            label: 'Description',
            value: item.description == null || item.description!.trim().isEmpty ? 'Non renseignee' : item.description!,
          ),
          _buildAttribute(
            label: 'Winget ID',
            value: item.wingetId,
          ),
          _buildAttribute(
            label: 'Commande',
            value: item.installCommand,
          ),
          _buildAttribute(
            label: 'Tags',
            value: item.tags == null || item.tags!.trim().isEmpty ? 'Non renseignes' : item.tags!,
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
            onPressed: () => Clipboard.setData(ClipboardData(text: item.installCommand)),
            icon: const Icon(Icons.copy),
            label: const Text('Copier la commande'),
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
