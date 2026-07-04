export interface SectionCreatePageProps {
  documentId?: string;
  parentId?: string;
  onCreated?: (createdSectionId: string, parentId?: string) => void | Promise<void>;
}
