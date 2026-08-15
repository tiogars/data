import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      'react-transition-group/TransitionGroupContext': 'react-transition-group/cjs/TransitionGroupContext.js',
    },
  },
  ssr: {
    noExternal: ['@mui/material', '@mui/system', '@mui/base', 'react-transition-group'],
  },
  test: {
    environment: 'jsdom',
    globals: true,
    setupFiles: ['./src/test/setupTests.ts'],
    include: ['src/**/*.test.ts', 'src/**/*.test.tsx'],
    coverage: {
      provider: 'v8',
      include: ['src/**/*.{ts,tsx}'],
      exclude: [
        'src/**/*.d.ts',
        'src/**/*.test.{ts,tsx}',
        'src/main.tsx',
        'src/services/**/*Api.ts',
        'src/test/**',
      ],
      reporter: ['text', 'html', 'lcov'],
      thresholds: {
        statements: 6,
        branches: 6,
        functions: 7,
        lines: 7,
      },
    },
    server: {
      deps: {
        inline: [/^@mui\//, /^react-transition-group$/],
      },
    },
  },
});
