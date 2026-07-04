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

export function filterSectionTree(sections: SectionTreeNode[], query: string): SectionTreeNode[] {
  const normalizedQuery = query.trim().toLowerCase();

  if (!normalizedQuery) {
    return sections;
  }

  return sections.flatMap((section) => {
    const filteredChildren = filterSectionTree(section.children, normalizedQuery);
    const matchesSelf = (
      (section.name ?? "").toLowerCase().includes(normalizedQuery)
      || (section.description ?? "").toLowerCase().includes(normalizedQuery)
      || formatSectionOrder(section).includes(normalizedQuery)
    );

    if (!matchesSelf && filteredChildren.length === 0) {
      return [];
    }

    return [
      {
        ...section,
        children: filteredChildren,
      },
    ];
  });
}

export function formatSectionOrder(section: Pick<SectionTreeNode, "displayOrder">): string {
  return String(section.displayOrder ?? 0);
}

export function collectExpandableIds(sections: SectionTreeNode[]): string[] {
  return sections.flatMap((section) => (
    section.children.length > 0
      ? [section.id, ...collectExpandableIds(section.children)]
      : []
  ));
}

export function collectTreeIds(sections: SectionTreeNode[]): string[] {
  return sections.flatMap((section) => [section.id, ...collectTreeIds(section.children)]);
}

export function findSectionPath(sections: SectionTreeNode[], targetId: string): string[] {
  for (const section of sections) {
    if (section.id === targetId) {
      return [section.id];
    }

    const childPath = findSectionPath(section.children, targetId);
    if (childPath.length > 0) {
      return [section.id, ...childPath];
    }
  }

  return [];
}
