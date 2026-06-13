import 'package:flutter/material.dart';
import 'package:flutter_application/features/vehicles/data/car_mileage_local_repository.dart';
import 'package:flutter_application/features/vehicles/domain/car_mileage_entry.dart';

class CarMileageOfflineFormPage extends StatefulWidget {
  const CarMileageOfflineFormPage({super.key});

  @override
  State<CarMileageOfflineFormPage> createState() => _CarMileageOfflineFormPageState();
}

class _CarMileageOfflineFormPageState extends State<CarMileageOfflineFormPage> {
  final _formKey = GlobalKey<FormState>();
  final _carIdController = TextEditingController();
  final _odometerController = TextEditingController();

  static const CarMileageLocalRepository _repository = CarMileageLocalRepository();

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
        carId: _carIdController.text.trim(),
        readingAt: DateTime.now().toUtc(),
        odometerKm: int.parse(_odometerController.text.trim()),
        updatedAt: DateTime.now().toUtc(),
      ),
    );

    if (!mounted) {
      return;
    }

    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('Kilometrage enregistre localement et ajoute a la file de synchro.')),
    );

    Navigator.of(context).pop(true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Nouveau kilometrage (offline)')),
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
                  child: const Text('Enregistrer hors ligne'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
