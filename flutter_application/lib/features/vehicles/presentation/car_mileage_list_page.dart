import 'package:flutter/material.dart';
import 'package:flutter_application/features/vehicles/data/car_mileage_local_repository.dart';
import 'package:flutter_application/features/vehicles/domain/car_item.dart';
import 'package:flutter_application/features/vehicles/domain/car_mileage_entry.dart';
import 'package:flutter_application/features/vehicles/presentation/car_mileage_offline_form_page.dart';
import 'package:intl/intl.dart';

class CarMileageListPage extends StatefulWidget {
  const CarMileageListPage({super.key, required this.car});

  final CarItem car;

  @override
  State<CarMileageListPage> createState() => _CarMileageListPageState();
}

class _CarMileageListPageState extends State<CarMileageListPage> {
  static const CarMileageLocalRepository _repository = CarMileageLocalRepository();
  late Future<List<CarMileageEntry>> _entriesFuture;

  static final DateFormat _dateFormat = DateFormat('dd/MM/yyyy HH:mm');

  @override
  void initState() {
    super.initState();
    _reload();
  }

  void _reload() {
    setState(() {
      _entriesFuture = _repository.findAll();
    });
  }

  Future<void> _openForm({CarMileageEntry? entry}) async {
    final saved = await Navigator.of(context).push<bool>(
      MaterialPageRoute(
        builder: (_) => CarMileageOfflineFormPage(
          entry: entry,
        ),
      ),
    );
    if (saved == true) _reload();
  }

  Future<void> _delete(CarMileageEntry entry) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Supprimer le kilometrage'),
        content: Text('Supprimer l\'entree du ${_dateFormat.format(entry.readingAt.toLocal())} (${entry.odometerKm} km) ?'),
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
      _reload();
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Kilometrages – ${widget.car.name}'),
      ),
      body: FutureBuilder<List<CarMileageEntry>>(
        future: _entriesFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) {
            return Center(child: Text('Erreur : ${snapshot.error}'));
          }
          final entries = snapshot.data ?? [];
          if (entries.isEmpty) {
            return const Center(child: Text('Aucun kilometrage enregistre.'));
          }
          return ListView.separated(
            itemCount: entries.length,
            separatorBuilder: (_, __) => const Divider(height: 1),
            itemBuilder: (context, index) {
              final entry = entries[index];
              return ListTile(
                leading: Icon(
                  Icons.speed,
                  color: entry.isDirty ? Colors.orange : Colors.grey,
                ),
                title: Text('${entry.odometerKm} km'),
                subtitle: Text(_dateFormat.format(entry.readingAt.toLocal())),
                trailing: IconButton(
                  icon: const Icon(Icons.delete_outline),
                  onPressed: () => _delete(entry),
                ),
                onTap: () => _openForm(entry: entry),
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _openForm(),
        tooltip: 'Nouveau kilometrage',
        child: const Icon(Icons.add),
      ),
    );
  }
}
