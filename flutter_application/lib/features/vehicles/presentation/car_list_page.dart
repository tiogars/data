import 'package:flutter/material.dart';
import 'package:flutter_application/features/vehicles/data/car_local_repository.dart';
import 'package:flutter_application/features/vehicles/domain/car_item.dart';
import 'package:flutter_application/features/vehicles/presentation/car_detail_page.dart';
import 'package:flutter_application/features/vehicles/presentation/car_offline_form_page.dart';

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

  Future<void> _openDetails(CarItem item) async {
    final changed = await Navigator.of(context).push<bool>(
      MaterialPageRoute(builder: (_) => CarDetailPage(item: item)),
    );
    if (changed == true) _reload();
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
                trailing: const Icon(Icons.chevron_right),
                onTap: () => _openDetails(item),
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
