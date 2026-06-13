import 'package:flutter/material.dart';
import 'package:flutter_application/features/gtin/data/gtin_local_repository.dart';
import 'package:flutter_application/features/gtin/domain/gtin_item.dart';

class GtinOfflineFormPage extends StatefulWidget {
  const GtinOfflineFormPage({super.key});

  @override
  State<GtinOfflineFormPage> createState() => _GtinOfflineFormPageState();
}

class _GtinOfflineFormPageState extends State<GtinOfflineFormPage> {
  final _formKey = GlobalKey<FormState>();
  final _codeController = TextEditingController();
  final _descriptionController = TextEditingController();

  static const GtinLocalRepository _repository = GtinLocalRepository();

  @override
  void dispose() {
    _codeController.dispose();
    _descriptionController.dispose();
    super.dispose();
  }

  Future<void> _save() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    await _repository.saveOffline(
      GtinItem(
        code: _codeController.text.trim(),
        description: _descriptionController.text.trim(),
        updatedAt: DateTime.now().toUtc(),
      ),
    );

    if (!mounted) {
      return;
    }

    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('GTIN enregistre localement et ajoute a la file de synchro.')),
    );

    Navigator.of(context).pop(true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Nouveau GTIN (offline)')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: _codeController,
                decoration: const InputDecoration(labelText: 'Code GTIN'),
                validator: (value) => (value == null || value.trim().isEmpty) ? 'Code requis' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _descriptionController,
                decoration: const InputDecoration(labelText: 'Description'),
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
