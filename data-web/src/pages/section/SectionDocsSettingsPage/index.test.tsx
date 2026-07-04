import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { SectionDocsSettingsPage } from './index';

const {
  useListSectionDocumentsQueryMock,
  useCreateSectionDocumentMutationMock,
  useUpdateSectionDocumentMutationMock,
  useDeleteSectionDocumentMutationMock,
} = vi.hoisted(() => ({
  useListSectionDocumentsQueryMock: vi.fn(),
  useCreateSectionDocumentMutationMock: vi.fn(),
  useUpdateSectionDocumentMutationMock: vi.fn(),
  useDeleteSectionDocumentMutationMock: vi.fn(),
}));

vi.mock('../../../services/sectionDocumentApi', () => ({
  useListSectionDocumentsQuery: useListSectionDocumentsQueryMock,
  useCreateSectionDocumentMutation: useCreateSectionDocumentMutationMock,
  useUpdateSectionDocumentMutation: useUpdateSectionDocumentMutationMock,
  useDeleteSectionDocumentMutation: useDeleteSectionDocumentMutationMock,
}));

describe('SectionDocsSettingsPage', () => {
  beforeEach(() => {
    const refetch = vi.fn();
    const createDocument = vi.fn().mockReturnValue({ unwrap: vi.fn().mockResolvedValue({}) });
    const updateDocument = vi.fn().mockReturnValue({ unwrap: vi.fn().mockResolvedValue({}) });
    const deleteDocument = vi.fn().mockReturnValue({ unwrap: vi.fn().mockResolvedValue({}) });

    useListSectionDocumentsQueryMock.mockReturnValue({
      data: {
        items: [
          { id: 'doc-1', name: 'Guides', storagePath: 'guides/existants' },
        ],
      },
      isLoading: false,
      error: undefined,
      refetch,
    });

    useCreateSectionDocumentMutationMock.mockReturnValue([createDocument, { isLoading: false }]);
    useUpdateSectionDocumentMutationMock.mockReturnValue([updateDocument, { isLoading: false }]);
    useDeleteSectionDocumentMutationMock.mockReturnValue([deleteDocument, { isLoading: false }]);
  });

  it('met a jour un document existant', async () => {
    const user = userEvent.setup();

    render(<SectionDocsSettingsPage />);

    expect(screen.getByRole('heading', { name: /Paramètres documents/i })).toBeInTheDocument();
    expect(screen.getByDisplayValue('guides/existants')).toBeInTheDocument();

    const pathField = screen.getAllByLabelText('Chemin relatif')[1];
    await user.clear(pathField);
    await user.type(pathField, 'guides/produits');
    await user.click(screen.getByRole('button', { name: /Mettre à jour/i }));

    const [updateDocument] = useUpdateSectionDocumentMutationMock.mock.results[0].value;
    const queryResult = useListSectionDocumentsQueryMock.mock.results[0].value;

    await waitFor(() => {
      expect(updateDocument).toHaveBeenCalledWith({
        id: 'doc-1',
        sectionDocument: {
          name: 'Guides',
          storagePath: 'guides/produits',
        },
      });
    });

    expect(queryResult.refetch).toHaveBeenCalledTimes(1);
  });
});