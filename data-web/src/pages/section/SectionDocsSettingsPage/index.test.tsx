import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { SectionDocsSettingsPage } from './index';

const {
  useListSectionsQueryMock,
  useGetSectionDocsSettingsStateQueryMock,
  useUpdateSectionDocsSettingsStateMutationMock,
} = vi.hoisted(() => ({
  useListSectionsQueryMock: vi.fn(),
  useGetSectionDocsSettingsStateQueryMock: vi.fn(),
  useUpdateSectionDocsSettingsStateMutationMock: vi.fn(),
}));

vi.mock('../../../services/sectionApi', () => ({
  useListSectionsQuery: useListSectionsQueryMock,
}));

vi.mock('../../../services/sectionDocsSettingsApi', () => ({
  useGetSectionDocsSettingsStateQuery: useGetSectionDocsSettingsStateQueryMock,
  useUpdateSectionDocsSettingsStateMutation: useUpdateSectionDocsSettingsStateMutationMock,
}));

describe('SectionDocsSettingsPage', () => {
  beforeEach(() => {
    const refetch = vi.fn();
    const unwrap = vi.fn().mockResolvedValue({ items: [] });
    const updateState = vi.fn().mockReturnValue({ unwrap });

    useListSectionsQueryMock.mockReturnValue({
      data: {
        items: [
          { id: 'root-1', name: 'Guides', description: 'Doc principale', displayOrder: 1 },
          { id: 'child-1', name: 'Installation', description: 'Fille', displayOrder: 2, parentId: 'root-1' },
        ],
      },
      isLoading: false,
      error: undefined,
    });

    useGetSectionDocsSettingsStateQueryMock.mockReturnValue({
      data: {
        items: [
          { id: 'setting-1', sectionId: 'root-1', storagePath: 'guides/existants' },
        ],
      },
      isLoading: false,
      error: undefined,
      refetch,
    });

    useUpdateSectionDocsSettingsStateMutationMock.mockReturnValue([updateState, { isLoading: false }]);
  });

  it('enregistre les chemins documentaires des sections racines uniquement', async () => {
    const user = userEvent.setup();

    render(<SectionDocsSettingsPage />);

    expect(screen.getByRole('heading', { name: /Paramètres docs des sections/i })).toBeInTheDocument();
    expect(screen.getByDisplayValue('guides/existants')).toBeInTheDocument();
    expect(screen.queryByText(/Installation/i)).not.toBeInTheDocument();

    const pathField = screen.getByLabelText('Chemin relatif');
    await user.clear(pathField);
    await user.type(pathField, 'guides/produits');
    await user.click(screen.getByRole('button', { name: /Enregistrer/i }));

    const [updateState] = useUpdateSectionDocsSettingsStateMutationMock.mock.results[0].value;
    const settingsQueryResult = useGetSectionDocsSettingsStateQueryMock.mock.results[0].value;

    await waitFor(() => {
      expect(updateState).toHaveBeenCalledWith({
        sectionDocsSettingsState: {
          items: [
            {
              id: 'setting-1',
              sectionId: 'root-1',
              storagePath: 'guides/produits',
            },
          ],
        },
      });
    });

    expect(settingsQueryResult.refetch).toHaveBeenCalledTimes(1);
  });
});