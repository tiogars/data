import 'package:flutter/material.dart';
import 'package:flutter_application/features/vehicles/data/car_local_repository.dart';
import 'package:flutter_application/features/vehicles/domain/car_item.dart';

class CarOfflineFormPage extends StatefulWidget {
  const CarOfflineFormPage({super.key});

  @override
  State<CarOfflineFormPage> createState() => _CarOfflineFormPageState();
}

class _CarOfflineFormPageState extends State<CarOfflineFormPage> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _plateController = TextEditingController();

  static const CarLocalRepository _repository = CarLocalRepository();

  @override
  void dispose() {
    _nameController.dispose();
    _plateController.dispose();
    super.dispose();
  }

  Future<void> _save() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    await _repository.saveOffline(
      CarItem(
        name: _nameController.text.trim(),
        plateNumber: _plateController.text.trim(),
        updatedAt: DateTime.now().toUtc(),
      ),
    );

    if (!mounted) {
      return;
    }

    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('Voiture enregistree localement et ajoutee a la file de synchro.')),
    );

    Navigator.of(context).pop(true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Nouvelle voiture (offline)')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: _nameController,
                decoration: const InputDecoration(labelText: 'Nom voiture'),
                validator: (value) => (value == null || value.trim().isEmpty) ? 'Nom requis' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _plateController,
                decoration: const InputDecoration(labelText: 'Immatriculation'),
                validator: (value) {
                  return (value == null || value.trim().isEmpty)
                      ? 'Immatriculation requise'
                      : null;
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
