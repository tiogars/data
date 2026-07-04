import { useMemo, type FC } from "react";
import Box from "@mui/material/Box";
import FormControl from "@mui/material/FormControl";
import FormHelperText from "@mui/material/FormHelperText";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import Select from "@mui/material/Select";
import { useListSectionsQuery, type Section } from "../services/sectionApi";
import { useSectionFormControllers } from "./useSectionFormControllers";

type SectionOption = {
  id: string;
  name: string;
  depth: number;
};

type SectionTreeNode = Omit<Section, "children"> & {
  id: string;
  children: SectionTreeNode[];
};

export interface SectionParentFieldProps {
  disabled?: boolean;
  excludedSectionId?: string;
  documentId?: string;
}

function toSectionTree(sections: Section[] | undefined): SectionTreeNode[] {
  return (sections ?? [])
    .filter((section): section is Section & { id: string } => Boolean(section.id))
    .map((section) => ({
      ...section,
      id: section.id,
      children: toSectionTree(section.children),
    }));
}

function findSectionById(sections: SectionTreeNode[], id: string): SectionTreeNode | null {
  for (const section of sections) {
    if (section.id === id) {
      return section;
    }

    const childMatch = findSectionById(section.children, id);
    if (childMatch) {
      return childMatch;
    }
  }

  return null;
}

function collectDescendantIds(section: SectionTreeNode): Set<string> {
  const ids = new Set<string>([section.id]);

  for (const child of section.children) {
    for (const childId of collectDescendantIds(child)) {
      ids.add(childId);
    }
  }

  return ids;
}

function flattenOptions(sections: SectionTreeNode[], excludedIds: Set<string>, depth = 0): SectionOption[] {
  return sections.flatMap((section) => {
    if (excludedIds.has(section.id)) {
      return [];
    }

    return [
      { id: section.id, name: section.name?.trim() || `Section ${section.id}`, depth },
      ...flattenOptions(section.children, excludedIds, depth + 1),
    ];
  });
}

const SectionParentField: FC<SectionParentFieldProps> = ({ disabled = false, excludedSectionId, documentId }) => {
  const { data, isLoading } = useListSectionsQuery({ documentId });
  const { parentIdController } = useSectionFormControllers();

  const options = useMemo(() => {
    const sectionTree = toSectionTree(data?.items);

    if (!excludedSectionId) {
      return flattenOptions(sectionTree, new Set<string>());
    }

    const excludedSection = findSectionById(sectionTree, excludedSectionId);
    const excludedIds = excludedSection ? collectDescendantIds(excludedSection) : new Set<string>([excludedSectionId]);

    return flattenOptions(sectionTree, excludedIds);
  }, [data?.items, excludedSectionId]);

  return (
    <FormControl fullWidth margin="normal" disabled={disabled || isLoading}>
      <InputLabel id="section-parent-label">Section parente</InputLabel>
      <Select
        labelId="section-parent-label"
        label="Section parente"
        name={parentIdController.field.name}
        value={parentIdController.field.value ?? ""}
        onBlur={parentIdController.field.onBlur}
        onChange={parentIdController.field.onChange}
        inputRef={parentIdController.field.ref}
      >
        <MenuItem value="">Aucune</MenuItem>
        {options.map((option) => (
          <MenuItem key={option.id} value={option.id}>
            <Box component="span" sx={{ pl: option.depth * 2, display: "inline-block" }}>
              {option.name}
            </Box>
          </MenuItem>
        ))}
      </Select>
      <FormHelperText>
        Rattachez cette section à une section existante du document sélectionné pour créer une sous-section.
      </FormHelperText>
    </FormControl>
  );
};

export default SectionParentField;