import 'package:flutter/material.dart';
import 'package:flutter_application/features/android_apps/data/android_app_local_repository.dart';
import 'package:flutter_application/features/android_apps/domain/android_app_item.dart';
import 'package:flutter_application/features/android_apps/presentation/android_app_offline_form_page.dart';
import 'package:intl/intl.dart';
import 'package:url_launcher/link.dart';

class AndroidAppDetailPage extends StatelessWidget {
  const AndroidAppDetailPage({super.key, required this.item});

  final AndroidAppItem item;

  static const AndroidAppLocalRepository _repository = AndroidAppLocalRepository();
  static final DateFormat _dateFormat = DateFormat('dd/MM/yyyy HH:mm');

  Future<void> _edit(BuildContext context) async {
    final saved = await Navigator.of(context).push<bool>(
      MaterialPageRoute(builder: (_) => AndroidAppOfflineFormPage(item: item)),
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
    final packageName = item.packageName.trim();
    final playStoreUri = packageName.isEmpty
        ? null
        : Uri.https(
            'play.google.com',
            '/store/apps/details',
            {'id': packageName},
          );

    return Scaffold(
      appBar: AppBar(title: const Text('Detail de l\'application')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          _buildAttribute(
            label: 'Nom',
            value: item.name,
          ),
          _buildAttribute(
            label: 'Package',
            value: item.packageName,
          ),
          _buildAttribute(
            label: 'Categorie',
            value: item.category == null || item.category!.trim().isEmpty ? 'Non renseignee' : item.category!,
          ),
          _buildAttribute(
            label: 'Description',
            value: item.description == null || item.description!.trim().isEmpty ? 'Non renseignee' : item.description!,
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
          if (playStoreUri != null) ...[
            const SizedBox(height: 12),
            Link(
              uri: playStoreUri,
              target: LinkTarget.blank,
              builder: (context, followLink) => OutlinedButton.icon(
                onPressed: followLink,
                icon: const Icon(Icons.open_in_new),
                label: const Text('Ouvrir sur le Play Store'),
              ),
            ),
          ],
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
