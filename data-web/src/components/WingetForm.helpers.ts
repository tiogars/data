export function formatTagsText(tags: string[] | undefined): string {
  return (tags ?? []).join(', ');
}

export function parseTagsText(value: string): string[] {
  return value
    .split(/[,|;]/)
    .map((item) => item.trim())
    .filter((item) => item.length > 0);
}

export function parseWingetIdsText(value: string): string[] {
  return value
    .split(/[\n\r,;|]/)
    .map((item) => item.trim())
    .filter((item) => item.length > 0);
}

export function buildWingetInstallCommand(wingetId: string): string {
  const trimmedWingetId = wingetId.trim();
  return trimmedWingetId.length > 0 ? `winget install -e --id ${trimmedWingetId}` : '';
}
