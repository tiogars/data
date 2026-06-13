import 'package:flutter/material.dart';
import 'package:flutter_application/features/android_apps/data/android_app_local_repository.dart';
import 'package:flutter_application/features/android_apps/domain/android_app_item.dart';

class AndroidAppOfflineFormPage extends StatefulWidget {
  const AndroidAppOfflineFormPage({super.key});

  @override
  State<AndroidAppOfflineFormPage> createState() => _AndroidAppOfflineFormPageState();
}

class _AndroidAppOfflineFormPageState extends State<AndroidAppOfflineFormPage> {
  final _formKey = GlobalKey<FormState>();
  final _nameController = TextEditingController();
  final _packageController = TextEditingController();
  final _categoryController = TextEditingController();

  static const AndroidAppLocalRepository _repository = AndroidAppLocalRepository();

  @override
  void dispose() {
    _nameController.dispose();
    _packageController.dispose();
    _categoryController.dispose();
    super.dispose();
  }

  Future<void> _save() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    await _repository.saveOffline(
      AndroidAppItem(
        name: _nameController.text.trim(),
        packageName: _packageController.text.trim(),
        category: _categoryController.text.trim().isEmpty ? null : _categoryController.text.trim(),
        updatedAt: DateTime.now().toUtc(),
      ),
    );

    if (!mounted) {
      return;
    }

    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('Application Android enregistree localement et ajoutee a la file de synchro.')),
    );

    Navigator.of(context).pop(true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Nouvelle app Android (offline)')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: _nameController,
                decoration: const InputDecoration(labelText: 'Nom'),
                validator: (value) => (value == null || value.trim().isEmpty) ? 'Nom requis' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _packageController,
                decoration: const InputDecoration(labelText: 'Package'),
                validator: (value) => (value == null || value.trim().isEmpty) ? 'Package requis' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _categoryController,
                decoration: const InputDecoration(labelText: 'Categorie'),
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
