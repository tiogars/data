import 'package:flutter/material.dart';
import 'package:flutter_application/features/winget_apps/data/winget_app_local_repository.dart';
import 'package:flutter_application/features/winget_apps/domain/winget_app_item.dart';
import 'package:flutter_application/features/winget_apps/presentation/winget_app_detail_page.dart';
import 'package:flutter_application/features/winget_apps/presentation/winget_app_offline_form_page.dart';

class WingetAppListPage extends StatefulWidget {
  const WingetAppListPage({super.key});

  @override
  State<WingetAppListPage> createState() => _WingetAppListPageState();
}

class _WingetAppListPageState extends State<WingetAppListPage> {
  static const WingetAppLocalRepository _repository = WingetAppLocalRepository();
  late Future<List<WingetAppItem>> _itemsFuture;

  @override
  void initState() {
    super.initState();
    _reload();
  }

  void _reload() {
    setState(() {
      _itemsFuture = _repository.findAll();
    });
  }

  Future<void> _openForm({WingetAppItem? item}) async {
    final saved = await Navigator.of(context).push<bool>(
      MaterialPageRoute(builder: (_) => WingetAppOfflineFormPage(item: item)),
    );
    if (saved == true) _reload();
  }

  Future<void> _openDetails(WingetAppItem item) async {
    final changed = await Navigator.of(context).push<bool>(
      MaterialPageRoute(builder: (_) => WingetAppDetailPage(item: item)),
    );
    if (changed == true) _reload();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Applications Winget')),
      body: FutureBuilder<List<WingetAppItem>>(
        future: _itemsFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }
          if (snapshot.hasError) {
            return Center(child: Text('Erreur : ${snapshot.error}'));
          }
          final items = snapshot.data ?? [];
          if (items.isEmpty) {
            return const Center(child: Text('Aucune application Winget enregistree.'));
          }
          return ListView.separated(
            itemCount: items.length,
            separatorBuilder: (_, _) => const Divider(height: 1),
            itemBuilder: (context, index) {
              final item = items[index];
              return ListTile(
                leading: Icon(
                  Icons.desktop_windows,
                  color: item.isDirty ? Colors.orange : Colors.grey,
                ),
                title: Text(item.name),
                subtitle: Text(item.wingetId),
                trailing: const Icon(Icons.chevron_right),
                onTap: () => _openDetails(item),
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _openForm(),
        tooltip: 'Nouvelle application Winget',
        child: const Icon(Icons.add),
      ),
    );
  }
}
