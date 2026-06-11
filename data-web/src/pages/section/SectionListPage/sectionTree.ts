import type { Section } from "../../../services/sectionApi";

export type SectionTreeNode = Omit<Section, "children"> & { id: string; children: SectionTreeNode[] };

export function toSectionTree(sections: Section[] | undefined): SectionTreeNode[] {
  return (sections ?? [])
    .filter((section): section is Section & { id: string } => Boolean(section.id))
    .map((section) => ({
      ...section,
      id: section.id,
      children: toSectionTree(section.children),
    }));
}

export function flattenSections(sections: SectionTreeNode[]): SectionTreeNode[] {
  return sections.flatMap((section) => [section, ...flattenSections(section.children)]);
}

export function collectExpandableIds(sections: SectionTreeNode[]): string[] {
  return sections.flatMap((section) => (
    section.children.length > 0
      ? [section.id, ...collectExpandableIds(section.children)]
      : []
  ));
}
