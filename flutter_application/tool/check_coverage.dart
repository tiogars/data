import 'dart:io';

void main(List<String> arguments) {
  final reportPath = arguments.isNotEmpty ? arguments[0] : 'coverage/lcov.info';
  final minimumPercent = arguments.length > 1 ? double.parse(arguments[1]) : 0.0;
  final report = File(reportPath);

  if (!report.existsSync()) {
    stderr.writeln('Rapport de couverture introuvable: $reportPath');
    exitCode = 1;
    return;
  }

  var linesFound = 0;
  var linesHit = 0;

  for (final line in report.readAsLinesSync()) {
    if (line.startsWith('LF:')) {
      linesFound += int.parse(line.substring(3));
    } else if (line.startsWith('LH:')) {
      linesHit += int.parse(line.substring(3));
    }
  }

  if (linesFound == 0) {
    stderr.writeln('Le rapport ne contient aucune ligne instrumentee.');
    exitCode = 1;
    return;
  }

  final percent = linesHit * 100 / linesFound;
  stdout.writeln(
    'Couverture Flutter: ${percent.toStringAsFixed(2)}% '
    '($linesHit/$linesFound lignes, minimum ${minimumPercent.toStringAsFixed(2)}%)',
  );

  if (percent < minimumPercent) {
    stderr.writeln('Le seuil minimal de couverture Flutter n est pas atteint.');
    exitCode = 1;
  }
}