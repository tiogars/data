import 'package:flutter/material.dart';
import 'package:flutter_application/features/gtin/data/gtin_local_repository.dart';
import 'package:flutter_application/features/gtin/domain/gtin_item.dart';

class GtinOfflineFormPage extends StatefulWidget {
  const GtinOfflineFormPage({super.key, this.item});

  final GtinItem? item;

  @override
  State<GtinOfflineFormPage> createState() => _GtinOfflineFormPageState();
}

class _GtinOfflineFormPageState extends State<GtinOfflineFormPage> {
  final _formKey = GlobalKey<FormState>();
  late final TextEditingController _codeController;
  late final TextEditingController _descriptionController;

  bool get _isEditing => widget.item != null;

  static const GtinLocalRepository _repository = GtinLocalRepository();

  @override
  void initState() {
    super.initState();
    _codeController = TextEditingController(text: widget.item?.code ?? '');
    _descriptionController = TextEditingController(text: widget.item?.description ?? '');
  }

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
        id: widget.item?.id,
        code: _codeController.text.trim(),
        description: _descriptionController.text.trim(),
        updatedAt: DateTime.now().toUtc(),
      ),
    );

    if (!mounted) {
      return;
    }

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(_isEditing ? 'GTIN mis a jour localement.' : 'GTIN enregistre localement et ajoute a la file de synchro.')),
    );

    Navigator.of(context).pop(true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(_isEditing ? 'Modifier GTIN' : 'Nouveau GTIN')),
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
