import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_application/core/auth/auth_service.dart';
import 'package:provider/provider.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  bool _isLoading = false;
  bool _autoResumeChecked = false;
  String? _error;

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    if (_autoResumeChecked) return;
    _autoResumeChecked = true;

    final shouldResume = context.read<AuthService>().takeAutoResumeLoginFlag();
    if (!shouldResume) return;

    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (!mounted || _isLoading) return;
      _login(
        loadingMessage:
            'Session interrompue pendant la connexion. Reprise automatique...',
      );
    });
  }

  Future<void> _login({String? loadingMessage}) async {
    setState(() {
      _isLoading = true;
      _error = loadingMessage;
    });

    try {
      await context.read<AuthService>().login();
      if (mounted && !context.read<AuthService>().isAuthenticated) {
        setState(() {
          _error =
              'Connexion incomplète. Le retour OIDC n\'a pas confirmé la session.';
        });
      }
    } on TimeoutException {
      if (mounted) {
        setState(() {
          _error =
              'Connexion expirée. Le callback OIDC n\'a pas été reçu (redirect URI / intent-filter).';
        });
      }
    } catch (_) {
      if (mounted) {
        setState(() {
          _error = 'Connexion échouée. Vérifier la configuration Keycloak et réessayer.';
        });
      }
    } finally {
      if (mounted) {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(32),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(
                'Data Mobile',
                style: Theme.of(context).textTheme.headlineMedium,
              ),
              const SizedBox(height: 8),
              Text(
                'Connexion requise',
                style: Theme.of(context).textTheme.bodyMedium,
              ),
              const SizedBox(height: 32),
              if (_error != null) ...[
                Text(
                  _error!,
                  textAlign: TextAlign.center,
                  style: TextStyle(color: Theme.of(context).colorScheme.error),
                ),
                const SizedBox(height: 16),
              ],
              FilledButton.icon(
                onPressed: _isLoading ? null : _login,
                icon: _isLoading
                    ? const SizedBox(
                        width: 16,
                        height: 16,
                        child: CircularProgressIndicator(strokeWidth: 2),
                      )
                    : const Icon(Icons.login),
                label: Text(_isLoading ? 'Connexion...' : 'Se connecter'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
