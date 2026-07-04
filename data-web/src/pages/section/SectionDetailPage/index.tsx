import type { FC } from "react";
import Accordion from "@mui/material/Accordion";
import AccordionDetails from "@mui/material/AccordionDetails";
import AccordionSummary from "@mui/material/AccordionSummary";
import Box from "@mui/material/Box";
import Divider from "@mui/material/Divider";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemText from "@mui/material/ListItemText";
import Paper from "@mui/material/Paper";
import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
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
  const { data: sectionsData } = useListSectionsQuery({ documentId: data?.documentId });

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
        <Typography variant="h4" component="h1">
          {data.name || "Sans nom"}
        </Typography>
        <Typography variant="body1" color="text.secondary">
          {data.description || "Aucune description fournie."}
        </Typography>

        <Accordion
          disableGutters
          elevation={0}
          sx={{
            border: 1,
            borderColor: "divider",
            borderRadius: 1,
            bgcolor: "background.default",
            '&::before': { display: "none" },
          }}
        >
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-controls="section-detail-content"
            id="section-detail-header"
            sx={{
              minHeight: 40,
              px: 1.5,
              '& .MuiAccordionSummary-content': {
                my: 0.75,
              },
              '&.Mui-expanded': {
                minHeight: 40,
              },
              '& .MuiAccordionSummary-content.Mui-expanded': {
                my: 0.75,
              },
            }}
          >
            <Typography variant="subtitle2" color="text.secondary" sx={{ fontWeight: 600 }}>
              Détail de la sélection
            </Typography>
          </AccordionSummary>
          <AccordionDetails sx={{ px: 1.5, pt: 0, pb: 1.5 }}>
            <Stack spacing={2}>
              <Divider />
              <Box>
                <Typography variant="overline" color="text.secondary">
                  ID
                </Typography>
                <Typography>{data.id}</Typography>
              </Box>
              <Box>
                <Typography variant="overline" color="text.secondary">
                  Ordre d'affichage
                </Typography>
                <Typography color="text.secondary">{data.displayOrder ?? 0}</Typography>
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
          </AccordionDetails>
        </Accordion>
      </Stack>
    </Paper>
  );
};
