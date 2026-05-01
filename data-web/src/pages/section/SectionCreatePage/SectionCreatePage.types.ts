export interface SectionCreatePageProps {
  parentId?: string;
  onCreated?: (createdSectionId: string, parentId?: string) => void | Promise<void>;
}
