import 'package:flutter/material.dart';
import 'package:flutter_application/features/vehicles/data/car_mileage_local_repository.dart';
import 'package:flutter_application/features/vehicles/domain/car_mileage_entry.dart';
import 'package:flutter_application/features/vehicles/presentation/car_mileage_offline_form_page.dart';
import 'package:intl/intl.dart';

class CarMileageDetailPage extends StatelessWidget {
  const CarMileageDetailPage({super.key, required this.entry});

  final CarMileageEntry entry;

  static const CarMileageLocalRepository _repository = CarMileageLocalRepository();
  static final DateFormat _dateFormat = DateFormat('dd/MM/yyyy HH:mm');

  Future<void> _edit(BuildContext context) async {
    final saved = await Navigator.of(context).push<bool>(
      MaterialPageRoute(
        builder: (_) => CarMileageOfflineFormPage(entry: entry, initialCarId: entry.carId),
      ),
    );
    if (saved == true && context.mounted) {
      Navigator.of(context).pop(true);
    }
  }

  Future<void> _delete(BuildContext context) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Supprimer le kilometrage'),
        content: Text(
          'Supprimer l\'entree du ${_dateFormat.format(entry.readingAt.toLocal())} (${entry.odometerKm} km) ?',
        ),
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
      await _repository.deleteOffline(entry);
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
      appBar: AppBar(title: const Text('Detail du kilometrage')),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          _buildAttribute(
            label: 'Date de releve',
            value: _dateFormat.format(entry.readingAt.toLocal()),
          ),
          _buildAttribute(
            label: 'Kilometrage',
            value: '${entry.odometerKm} km',
          ),
          _buildAttribute(
            label: 'Volume carburant',
            value: entry.fuelVolumeLiters != null
                ? '${entry.fuelVolumeLiters} L'
                : 'Non renseigne',
          ),
          _buildAttribute(
            label: 'Plein complet',
            value: entry.fullTank ? 'Oui' : 'Non',
          ),
          _buildAttribute(
            label: 'Derniere mise a jour',
            value: _dateFormat.format(entry.updatedAt.toLocal()),
          ),
          _buildAttribute(
            label: 'Statut synchro',
            value: entry.isDirty ? 'Synchronisation en attente' : 'Synchronise',
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
