import '@testing-library/jest-dom/vitest';
import { vi } from 'vitest';

vi.mock('react-transition-group/TransitionGroupContext', () => ({
  __esModule: true,
  default: {},
  TransitionGroupContext: {},
}));

if (!globalThis.matchMedia) {
  Object.defineProperty(globalThis, 'matchMedia', {
    writable: true,
    value: (query: string) => ({
      matches: false,
      media: query,
      onchange: null,
      addListener: () => {},
      removeListener: () => {},
      addEventListener: () => {},
      removeEventListener: () => {},
      dispatchEvent: () => false,
    }),
  });
}
