import 'package:flutter/material.dart';
import 'package:flutter_application/features/vehicles/data/car_mileage_local_repository.dart';
import 'package:flutter_application/features/vehicles/domain/car_mileage_entry.dart';

class CarMileageOfflineFormPage extends StatefulWidget {
  const CarMileageOfflineFormPage({super.key, this.entry});

  final CarMileageEntry? entry;

  @override
  State<CarMileageOfflineFormPage> createState() => _CarMileageOfflineFormPageState();
}

class _CarMileageOfflineFormPageState extends State<CarMileageOfflineFormPage> {
  final _formKey = GlobalKey<FormState>();
  late final TextEditingController _carIdController;
  late final TextEditingController _odometerController;

  bool get _isEditing => widget.entry != null;

  static const CarMileageLocalRepository _repository = CarMileageLocalRepository();

  @override
  void initState() {
    super.initState();
    _carIdController = TextEditingController(
      text: widget.entry?.carId ?? '',
    );
    _odometerController = TextEditingController(
      text: widget.entry != null ? widget.entry!.odometerKm.toString() : '',
    );
  }

  @override
  void dispose() {
    _carIdController.dispose();
    _odometerController.dispose();
    super.dispose();
  }

  Future<void> _save() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    await _repository.saveOffline(
      CarMileageEntry(
        id: widget.entry?.id,
        carId: _carIdController.text.trim(),
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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(_isEditing ? 'Modifier le kilometrage' : 'Nouveau kilometrage')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: _carIdController,
                decoration: const InputDecoration(labelText: 'ID voiture (UUID backend)'),
                validator: (value) {
                  final input = value?.trim() ?? '';
                  return input.isEmpty ? 'ID voiture requis' : null;
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
                  onPressed: _save,
                  child: Text(_isEditing ? 'Mettre a jour' : 'Enregistrer hors ligne'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
