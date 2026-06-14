import 'package:flutter/material.dart';
import 'package:flutter_application/features/android_apps/data/android_app_local_repository.dart';
import 'package:flutter_application/features/android_apps/domain/android_app_item.dart';

class AndroidAppOfflineFormPage extends StatefulWidget {
  const AndroidAppOfflineFormPage({super.key, this.item});

  final AndroidAppItem? item;

  @override
  State<AndroidAppOfflineFormPage> createState() => _AndroidAppOfflineFormPageState();
}

class _AndroidAppOfflineFormPageState extends State<AndroidAppOfflineFormPage> {
  final _formKey = GlobalKey<FormState>();
  late final TextEditingController _nameController;
  late final TextEditingController _packageController;
  late final TextEditingController _categoryController;

  bool get _isEditing => widget.item != null;

  static const AndroidAppLocalRepository _repository = AndroidAppLocalRepository();

  @override
  void initState() {
    super.initState();
    _nameController = TextEditingController(text: widget.item?.name ?? '');
    _packageController = TextEditingController(text: widget.item?.packageName ?? '');
    _categoryController = TextEditingController(text: widget.item?.category ?? '');
  }

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
        id: widget.item?.id,
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
      SnackBar(content: Text(_isEditing ? 'Application mise a jour localement.' : 'Application Android enregistree localement et ajoutee a la file de synchro.')),
    );

    Navigator.of(context).pop(true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(_isEditing ? 'Modifier l\'application' : 'Nouvelle app Android')),
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
