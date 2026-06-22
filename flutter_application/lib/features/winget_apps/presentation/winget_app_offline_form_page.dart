import 'package:flutter/material.dart';
import 'package:flutter_application/features/winget_apps/data/winget_app_local_repository.dart';
import 'package:flutter_application/features/winget_apps/domain/winget_app_item.dart';

class WingetAppOfflineFormPage extends StatefulWidget {
  const WingetAppOfflineFormPage({super.key, this.item});

  final WingetAppItem? item;

  @override
  State<WingetAppOfflineFormPage> createState() => _WingetAppOfflineFormPageState();
}

class _WingetAppOfflineFormPageState extends State<WingetAppOfflineFormPage> {
  final _formKey = GlobalKey<FormState>();
  late final TextEditingController _nameController;
  late final TextEditingController _descriptionController;
  late final TextEditingController _wingetIdController;
  late final TextEditingController _installCommandController;
  late final TextEditingController _tagsController;

  bool get _isEditing => widget.item != null;

  static const WingetAppLocalRepository _repository = WingetAppLocalRepository();

  @override
  void initState() {
    super.initState();
    _nameController = TextEditingController(text: widget.item?.name ?? '');
    _descriptionController = TextEditingController(text: widget.item?.description ?? '');
    _wingetIdController = TextEditingController(text: widget.item?.wingetId ?? '');
    _installCommandController = TextEditingController(text: widget.item?.installCommand ?? '');
    _tagsController = TextEditingController(text: widget.item?.tags ?? '');
  }

  @override
  void dispose() {
    _nameController.dispose();
    _descriptionController.dispose();
    _wingetIdController.dispose();
    _installCommandController.dispose();
    _tagsController.dispose();
    super.dispose();
  }

  Future<void> _save() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    await _repository.saveOffline(
      WingetAppItem(
        id: widget.item?.id,
        name: _nameController.text.trim(),
        description: _descriptionController.text.trim().isEmpty ? null : _descriptionController.text.trim(),
        wingetId: _wingetIdController.text.trim(),
        installCommand: _installCommandController.text.trim(),
        tags: _tagsController.text.trim().isEmpty ? null : _tagsController.text.trim(),
        updatedAt: DateTime.now().toUtc(),
      ),
    );

    if (!mounted) {
      return;
    }

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(_isEditing ? 'Application mise a jour localement.' : 'Application Winget enregistree localement et ajoutee a la file de synchro.')),
    );

    Navigator.of(context).pop(true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(_isEditing ? 'Modifier l\'application Winget' : 'Nouvelle app Winget')),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: ListView(
            children: [
              TextFormField(
                controller: _nameController,
                decoration: const InputDecoration(labelText: 'Nom'),
                validator: (value) => (value == null || value.trim().isEmpty) ? 'Nom requis' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _descriptionController,
                decoration: const InputDecoration(labelText: 'Description'),
                minLines: 2,
                maxLines: 4,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _wingetIdController,
                decoration: const InputDecoration(labelText: 'Winget ID'),
                validator: (value) => (value == null || value.trim().isEmpty) ? 'Winget ID requis' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _installCommandController,
                decoration: const InputDecoration(labelText: 'Commande installation'),
                validator: (value) => (value == null || value.trim().isEmpty) ? 'Commande requise' : null,
              ),
              const SizedBox(height: 12),
              TextFormField(
                controller: _tagsController,
                decoration: const InputDecoration(labelText: 'Tags (separes par virgule)'),
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
