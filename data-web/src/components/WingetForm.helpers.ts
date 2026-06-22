export function formatTagsText(tags: string[] | undefined): string {
  return (tags ?? []).join(', ');
}

export function parseTagsText(value: string): string[] {
  return value
    .split(/[,|;]/)
    .map((item) => item.trim())
    .filter((item) => item.length > 0);
}
