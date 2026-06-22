import { describe, expect, it } from 'vitest';

import { formatTagsText, parseTagsText } from './WingetForm.helpers';

describe('WingetForm tags helpers', () => {
  it('shouldParseTagsWhenSeparatorsMixed', () => {
    expect(parseTagsText('editor, windows; cli|windows')).toEqual(['editor', 'windows', 'cli', 'windows']);
  });

  it('shouldFormatTagsAsCommaSeparatedString', () => {
    expect(formatTagsText(['editor', 'windows'])).toBe('editor, windows');
  });
});
