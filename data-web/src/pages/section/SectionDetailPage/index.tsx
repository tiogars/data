import type { FC } from "react";
import Box from "@mui/material/Box";
import Divider from "@mui/material/Divider";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemText from "@mui/material/ListItemText";
import Paper from "@mui/material/Paper";
import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";
import { useGetSectionByIdQuery, useListSectionsQuery, type Section } from "../../../services/sectionApi";
import type { SectionDetailPageProps } from "./SectionDetailPage.types";

type SectionTreeNode = Omit<Section, "children"> & {
  id: string;
  children: SectionTreeNode[];
};

function toSectionTree(sections: Section[] | undefined): SectionTreeNode[] {
  return (sections ?? [])
    .filter((section): section is Section & { id: string } => Boolean(section.id))
    .map((section) => ({
      ...section,
      id: section.id,
      children: toSectionTree(section.children),
    }));
}

function flattenSections(sections: SectionTreeNode[]): SectionTreeNode[] {
  return sections.flatMap((section) => [section, ...flattenSections(section.children)]);
}

const SubsectionList: FC<{ sections: SectionTreeNode[]; onSelectSection?: (id: string) => void; depth?: number }> = ({
  sections,
  onSelectSection,
  depth = 0,
}) => {
  if (sections.length === 0) {
    return <Typography color="text.secondary">Aucune sous-section.</Typography>;
  }

  return (
    <List sx={{ py: 0 }}>
      {sections.map((section) => (
        <Box key={section.id} sx={{ pl: depth * 2 }}>
          <ListItem disablePadding>
            <ListItemButton onClick={() => onSelectSection?.(section.id)}>
              <ListItemText
                primary={section.name || "Sans nom"}
                secondary={section.description || "Aucune description fournie."}
              />
            </ListItemButton>
          </ListItem>
          {section.children.length > 0 && (
            <SubsectionList sections={section.children} onSelectSection={onSelectSection} depth={depth + 1} />
          )}
        </Box>
      ))}
    </List>
  );
};

export const SectionDetailPage: FC<SectionDetailPageProps> = ({ id, onSelectSection }) => {
  const { data, isLoading, error } = useGetSectionByIdQuery({ id });
  const { data: sectionsData } = useListSectionsQuery();

  const sectionsById = flattenSections(toSectionTree(sectionsData?.items)).reduce<Record<string, SectionTreeNode>>((acc, section) => {
    acc[section.id] = section;
    return acc;
  }, {});

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de la section</div>;
  if (!data) return <div>Section introuvable</div>;

  return (
    <Paper variant="outlined" sx={{ p: 3 }}>
      <Stack spacing={2}>
        <Typography variant="h6">Détail de la section</Typography>
        <Divider />
        <Box>
          <Typography variant="overline" color="text.secondary">
            ID
          </Typography>
          <Typography>{data.id}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">
            Nom
          </Typography>
          <Typography>{data.name || "Sans nom"}</Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">
            Description
          </Typography>
          <Typography color="text.secondary">
            {data.description || "Aucune description fournie."}
          </Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">
            Section parente
          </Typography>
          <Typography color="text.secondary">
            {data.parentId ? (sectionsById[data.parentId]?.name || data.parentId) : "Aucune"}
          </Typography>
        </Box>
        <Box>
          <Typography variant="overline" color="text.secondary">
            Sous-sections
          </Typography>
          <SubsectionList sections={toSectionTree(data.children)} onSelectSection={onSelectSection} />
        </Box>
      </Stack>
    </Paper>
  );
};
