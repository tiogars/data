import 'package:flutter/material.dart';
import 'package:flutter_application/features/vehicles/data/car_local_repository.dart';
import 'package:flutter_application/features/vehicles/domain/car_item.dart';
import 'package:flutter_application/features/vehicles/data/car_mileage_local_repository.dart';
import 'package:flutter_application/features/vehicles/domain/car_mileage_entry.dart';

class CarMileageOfflineFormPage extends StatefulWidget {
  const CarMileageOfflineFormPage({super.key, this.entry, this.initialCarId});

  final CarMileageEntry? entry;
  final String? initialCarId;

  @override
  State<CarMileageOfflineFormPage> createState() => _CarMileageOfflineFormPageState();
}

class _CarMileageOfflineFormPageState extends State<CarMileageOfflineFormPage> {
  final _formKey = GlobalKey<FormState>();
  late final TextEditingController _odometerController;
  late final Future<List<CarItem>> _carsFuture;
  String? _selectedCarId;

  bool get _isEditing => widget.entry != null;

  static const CarMileageLocalRepository _repository = CarMileageLocalRepository();
  static const CarLocalRepository _carRepository = CarLocalRepository();

  @override
  void initState() {
    super.initState();
    _carsFuture = _carRepository.findAll();
    _selectedCarId = widget.entry?.carId ?? widget.initialCarId;
    _odometerController = TextEditingController(
      text: widget.entry != null ? widget.entry!.odometerKm.toString() : '',
    );
  }

  @override
  void dispose() {
    _odometerController.dispose();
    super.dispose();
  }

  Future<void> _save() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    final selectedCarId = _selectedCarId?.trim() ?? '';

    await _repository.saveOffline(
      CarMileageEntry(
        id: widget.entry?.id,
        carId: selectedCarId,
        readingAt: widget.entry?.readingAt ?? DateTime.now().toUtc(),
        odometerKm: int.parse(_odometerController.text.trim()),
        fuelVolumeLiters: widget.entry?.fuelVolumeLiters,
        fullTank: widget.entry?.fullTank ?? false,
        updatedAt: DateTime.now().toUtc(),
      ),
    );

    if (!mounted) {
      return;
    }

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(_isEditing ? 'Kilometrage mis a jour localement.' : 'Kilometrage enregistre localement et ajoute a la file de synchro.')),
    );

    Navigator.of(context).pop(true);
  }

  List<DropdownMenuItem<String>> _buildCarItems(List<CarItem> cars) {
    final items = cars
        .where((car) => (car.remoteId ?? '').trim().isNotEmpty)
        .map(
          (car) => DropdownMenuItem<String>(
            value: car.remoteId,
            child: Text(_carLabel(car)),
          ),
        )
        .toList();

    final selectedCarId = _selectedCarId?.trim();
    final hasSelected = selectedCarId != null && selectedCarId.isNotEmpty;
    final existsInItems = items.any((item) => item.value == selectedCarId);

    if (hasSelected && !existsInItems) {
      items.insert(
        0,
        DropdownMenuItem<String>(
          value: selectedCarId,
          child: Text('Vehicule inconnu ($selectedCarId)'),
        ),
      );
    }

    return items;
  }

  String _carLabel(CarItem car) {
    final plateNumber = car.plateNumber?.trim() ?? '';
    if (plateNumber.isEmpty) {
      return car.name;
    }
    return '${car.name} - $plateNumber';
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(_isEditing ? 'Modifier le kilometrage' : 'Nouveau kilometrage')),
      body: FutureBuilder<List<CarItem>>(
        future: _carsFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            return Center(child: Text('Erreur : ${snapshot.error}'));
          }

          final carItems = _buildCarItems(snapshot.data ?? []);
          final hasSelectableCars = carItems.isNotEmpty;

          return Padding(
            padding: const EdgeInsets.all(16),
            child: Form(
              key: _formKey,
              child: Column(
                children: [
                  DropdownButtonFormField<String>(
                    initialValue: hasSelectableCars ? _selectedCarId : null,
                    decoration: InputDecoration(
                      labelText: 'Voiture',
                      helperText: hasSelectableCars ? null : 'Synchronisez d\'abord des voitures avec un UUID backend.',
                    ),
                    items: carItems,
                    onChanged: hasSelectableCars
                        ? (value) {
                            setState(() {
                              _selectedCarId = value;
                            });
                          }
                        : null,
                    validator: (value) {
                      final input = value?.trim() ?? '';
                      return input.isEmpty ? 'Voiture requise' : null;
                    },
                  ),
                  const SizedBox(height: 12),
                  TextFormField(
                    controller: _odometerController,
                    keyboardType: TextInputType.number,
                    decoration: const InputDecoration(labelText: 'Kilometrage (km)'),
                    validator: (value) {
                      final parsed = int.tryParse(value?.trim() ?? '');
                      return parsed == null ? 'Kilometrage invalide' : null;
                    },
                  ),
                  const SizedBox(height: 20),
                  SizedBox(
                    width: double.infinity,
                    child: FilledButton(
                      onPressed: hasSelectableCars ? _save : null,
                      child: Text(_isEditing ? 'Mettre a jour' : 'Enregistrer hors ligne'),
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
