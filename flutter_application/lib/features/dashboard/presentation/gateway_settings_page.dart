import 'package:flutter/material.dart';
import 'package:flutter_application/core/api/mobile_runtime_config.dart';

class GatewaySettingsPage extends StatefulWidget {
  const GatewaySettingsPage({
    super.key,
    required this.initialConfig,
    required this.onSave,
  });

  final MobileRuntimeConfig initialConfig;
  final Future<void> Function(MobileRuntimeConfig config) onSave;

  @override
  State<GatewaySettingsPage> createState() => _GatewaySettingsPageState();
}

class _GatewaySettingsPageState extends State<GatewaySettingsPage> {
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();
  late final TextEditingController _gatewayController;
  late final TextEditingController _jwtController;
  bool _isSaving = false;

  @override
  void initState() {
    super.initState();
    _gatewayController = TextEditingController(text: widget.initialConfig.gatewayBaseUrl);
    _jwtController = TextEditingController(text: widget.initialConfig.jwtToken ?? '');
  }

  @override
  void didUpdateWidget(covariant GatewaySettingsPage oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.initialConfig != widget.initialConfig && !_isSaving) {
      _gatewayController.text = widget.initialConfig.gatewayBaseUrl;
      _jwtController.text = widget.initialConfig.jwtToken ?? '';
    }
  }

  @override
  void dispose() {
    _gatewayController.dispose();
    _jwtController.dispose();
    super.dispose();
  }

  Future<void> _save() async {
    if (!_formKey.currentState!.validate() || _isSaving) {
      return;
    }

    final token = _jwtController.text.trim();
    final config = MobileRuntimeConfig(
      gatewayBaseUrl: _gatewayController.text.trim(),
      jwtToken: token.isEmpty ? null : token,
    );

    await _persistConfig(
      config,
      successMessage: 'Configuration gateway enregistree.',
    );
  }

  Future<void> _resetConfiguration() async {
    if (_isSaving) {
      return;
    }

    _gatewayController.text = MobileRuntimeConfig.local.gatewayBaseUrl;
    _jwtController.clear();

    await _persistConfig(
      MobileRuntimeConfig.local,
      successMessage: 'Configuration gateway reinitialisee.',
    );
  }

  Future<void> _persistConfig(
    MobileRuntimeConfig config, {
    required String successMessage,
  }) async {
    if (_isSaving) {
      return;
    }

    setState(() {
      _isSaving = true;
    });

    try {
      await widget.onSave(config);
      if (!mounted) {
        return;
      }

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(successMessage)),
      );
    } finally {
      if (mounted) {
        setState(() {
          _isSaving = false;
        });
      }
    }
  }

  String? _validateGatewayUrl(String? value) {
    final raw = value?.trim() ?? '';
    if (raw.isEmpty) {
      return 'Renseigner une URL de gateway.';
    }

    final uri = Uri.tryParse(raw);
    if (uri == null || !uri.hasScheme || !uri.hasAuthority) {
      return 'URL invalide.';
    }

    if (uri.scheme != 'http' && uri.scheme != 'https') {
      return 'Utiliser http ou https.';
    }

    return null;
  }

  @override
  Widget build(BuildContext context) {
    return Form(
      key: _formKey,
      child: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          Text(
            'Parametrage gateway',
            style: Theme.of(context).textTheme.headlineSmall,
          ),
          const SizedBox(height: 8),
          Text(
            'Definir l URL de la gateway. En production, la connexion utilise Keycloak (OIDC).',
            style: Theme.of(context).textTheme.bodyMedium,
          ),
          const SizedBox(height: 16),
          TextFormField(
            controller: _gatewayController,
            decoration: const InputDecoration(
              labelText: 'URL gateway',
              hintText: 'https://gw.data.tiogars.fr',
              border: OutlineInputBorder(),
            ),
            validator: _validateGatewayUrl,
            keyboardType: TextInputType.url,
            textInputAction: TextInputAction.next,
          ),
          const SizedBox(height: 12),
          TextFormField(
            controller: _jwtController,
            decoration: const InputDecoration(
              labelText: 'JWT token (debug – bypass OIDC)',
              helperText: 'Laisser vide en production. Utilisé uniquement comme override développeur.',
              helperMaxLines: 2,
              border: OutlineInputBorder(),
            ),
            minLines: 1,
            maxLines: 3,
          ),
          const SizedBox(height: 16),
          OutlinedButton.icon(
            onPressed: _isSaving ? null : _resetConfiguration,
            icon: const Icon(Icons.restart_alt),
            label: const Text('Reinitialiser la configuration'),
          ),
          const SizedBox(height: 8),
          FilledButton.icon(
            onPressed: _isSaving ? null : _save,
            icon: _isSaving
                ? const SizedBox(
                    width: 16,
                    height: 16,
                    child: CircularProgressIndicator(strokeWidth: 2),
                  )
                : const Icon(Icons.save),
            label: Text(_isSaving ? 'Enregistrement...' : 'Enregistrer'),
          ),
        ],
      ),
    );
  }
}
