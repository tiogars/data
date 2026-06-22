import { describe, expect, it } from 'vitest';

import {
  buildWingetInstallCommand,
  formatTagsText,
  parseTagsText,
} from './WingetForm.helpers';

describe('WingetForm tags helpers', () => {
  it('shouldParseTagsWhenSeparatorsMixed', () => {
    expect(parseTagsText('editor, windows; cli|windows')).toEqual(['editor', 'windows', 'cli', 'windows']);
  });

  it('shouldFormatTagsAsCommaSeparatedString', () => {
    expect(formatTagsText(['editor', 'windows'])).toBe('editor, windows');
  });
});

describe('WingetForm wingetId helpers', () => {
  it('shouldBuildInstallCommandFromWingetId', () => {
    expect(buildWingetInstallCommand('Microsoft.VisualStudioCode')).toBe('winget install -e --id Microsoft.VisualStudioCode');
  });
});
