import 'package:flutter/material.dart';
import 'package:flutter_application/features/vehicles/data/car_local_repository.dart';
import 'package:flutter_application/features/vehicles/domain/car_item.dart';
import 'package:flutter_application/features/vehicles/presentation/car_offline_form_page.dart';
import 'package:flutter_application/features/vehicles/presentation/car_mileage_list_page.dart';

class CarListPage extends StatefulWidget {
  const CarListPage({super.key});

  @override
  State<CarListPage> createState() => _CarListPageState();
}

class _CarListPageState extends State<CarListPage> {
  static const CarLocalRepository _repository = CarLocalRepository();
  late Future<List<CarItem>> _itemsFuture;

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

  Future<void> _openForm({CarItem? item}) async {
    final saved = await Navigator.of(context).push<bool>(
      MaterialPageRoute(builder: (_) => CarOfflineFormPage(item: item)),
    );
    if (saved == true) _reload();
  }

  Future<void> _openMileage(CarItem item) async {
    await Navigator.of(context).push(
      MaterialPageRoute(
        builder: (_) => CarMileageListPage(car: item),
      ),
    );
  }

  Future<void> _delete(CarItem item) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Supprimer la voiture'),
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
      appBar: AppBar(title: const Text('Voitures')),
      body: FutureBuilder<List<CarItem>>(
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
            return const Center(child: Text('Aucune voiture enregistree.'));
          }
          return ListView.separated(
            itemCount: items.length,
            separatorBuilder: (_, _) => const Divider(height: 1),
            itemBuilder: (context, index) {
              final item = items[index];
              return ListTile(
                leading: Icon(
                  Icons.directions_car,
                  color: item.isDirty ? Colors.orange : Colors.grey,
                ),
                title: Text(item.name),
                subtitle: item.plateNumber != null && item.plateNumber!.isNotEmpty
                    ? Text(item.plateNumber!)
                    : null,
                trailing: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    IconButton(
                      icon: const Icon(Icons.speed),
                      tooltip: 'Kilometrages',
                      onPressed: () => _openMileage(item),
                    ),
                    IconButton(
                      icon: const Icon(Icons.delete_outline),
                      onPressed: () => _delete(item),
                    ),
                  ],
                ),
                onTap: () => _openForm(item: item),
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _openForm(),
        tooltip: 'Nouvelle voiture',
        child: const Icon(Icons.add),
      ),
    );
  }
}
