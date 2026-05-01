import { useEffect, useMemo, useState, type FC } from "react";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Chip from "@mui/material/Chip";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import Divider from "@mui/material/Divider";
import IconButton from "@mui/material/IconButton";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemText from "@mui/material/ListItemText";
import Paper from "@mui/material/Paper";
import Stack from "@mui/material/Stack";
import Tab from "@mui/material/Tab";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Tabs from "@mui/material/Tabs";
import Tooltip from "@mui/material/Tooltip";
import Typography from "@mui/material/Typography";
import useMediaQuery from "@mui/material/useMediaQuery";
import AddIcon from "@mui/icons-material/Add";
import { useTheme } from "@mui/material/styles";
import { SectionCreatePage } from "../SectionCreatePage";
import { SectionDetailPage } from "../SectionDetailPage";
import { SectionEditPage } from "../SectionEditPage";
import { useListSectionsQuery, useDeleteSectionByIdMutation, type Section } from "../../../services/sectionApi";
import type { SectionListPageProps } from "./SectionListPage.types";

type PanelMode = "view" | "edit" | "create";

function getPanelSubtitleText(panelMode: PanelMode, selectedSection: (Section & { id: string }) | null): string {
  if (panelMode === "create") return "Remplissez le formulaire pour créer une nouvelle section.";
  if (selectedSection) return selectedSection.description?.trim() || "Cette section n'a pas encore de description.";
  return "Choisissez une section dans la navigation pour commencer.";
}

const PanelSubtitle: FC<{ panelMode: PanelMode; selectedSection: (Section & { id: string }) | null }> = ({ panelMode, selectedSection }) => (
  <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
    {getPanelSubtitleText(panelMode, selectedSection)}
  </Typography>
);

type SectionTreeNode = Omit<Section, "children"> & { id: string; children: SectionTreeNode[] };
type SectionRow = SectionTreeNode & { depth: number; isLast: boolean; ancestorLines: boolean[] };

function toSectionTree(sections: Section[] | undefined): SectionTreeNode[] {
  return (sections ?? [])
    .filter((section): section is Section & { id: string } => Boolean(section.id))
    .map((section) => ({
      ...section,
      id: section.id,
      children: toSectionTree(section.children),
    }));
}

function flattenSections(sections: SectionTreeNode[], depth = 0, ancestorLines: boolean[] = []): SectionRow[] {
  return sections.flatMap((section, index) => {
    const isLast = index === sections.length - 1;
    return [
      { ...section, depth, isLast, ancestorLines },
      ...flattenSections(section.children, depth + 1, [...ancestorLines, !isLast]),
    ];
  });
}

const TREE_UNIT = 16;

const TreeConnector: FC<{ ancestorLines: boolean[]; isLast: boolean }> = ({ ancestorLines, isLast }) => (
  <Box
    component="span"
    sx={{ display: 'inline-flex', alignItems: 'center', height: 24, mr: 0.5, verticalAlign: 'middle', flexShrink: 0 }}
  >
    {ancestorLines.map((hasLine, i) => (
      <Box
        key={`depth-${i}`}
        component="span"
        sx={{
          display: 'inline-block',
          width: TREE_UNIT,
          height: 24,
          position: 'relative',
          flexShrink: 0,
          ...(hasLine && {
            '&::before': {
              content: '""',
              position: 'absolute',
              left: '50%',
              top: 0,
              bottom: 0,
              borderLeft: '1.5px solid',
              borderColor: 'text.disabled',
            },
          }),
        }}
      />
    ))}
    <Box
      component="span"
      sx={{
        display: 'inline-block',
        width: TREE_UNIT,
        height: 24,
        position: 'relative',
        flexShrink: 0,
        '&::before': {
          content: '""',
          position: 'absolute',
          left: '50%',
          top: 0,
          bottom: isLast ? '50%' : 0,
          borderLeft: '1.5px solid',
          borderColor: 'text.disabled',
        },
        '&::after': {
          content: '""',
          position: 'absolute',
          left: '50%',
          right: 0,
          top: '50%',
          borderTop: '1.5px solid',
          borderColor: 'text.disabled',
        },
      }}
    />
  </Box>
);

type NavigationListProps = {
  sections: SectionTreeNode[];
  selectedSectionId: string | null;
  isMobile: boolean;
  onSelect: (id: string) => void;
  onAddChild: (parentId: string) => void;
};

const NavigationList: FC<NavigationListProps> = ({ sections, selectedSectionId, isMobile, onSelect, onAddChild }) => {
  if (sections.length === 0) return <Typography color="text.secondary">Aucune section disponible.</Typography>;

  const rows = flattenSections(sections);

  if (isMobile) {
    return (
      <List sx={{ p: 0 }}>
        {rows.map((section) => (
          <ListItem key={section.id} disablePadding secondaryAction={
            section.id === selectedSectionId ? (
              <Tooltip title="Ajouter un enfant">
                <IconButton
                  size="small"
                  onClick={(e) => { e.stopPropagation(); onAddChild(section.id); }}
                  aria-label="Ajouter un enfant"
                >
                  <AddIcon fontSize="small" />
                </IconButton>
              </Tooltip>
            ) : undefined
          }>
            <ListItemButton
              selected={section.id === selectedSectionId}
              onClick={() => onSelect(section.id)}
              sx={{ borderRadius: 1, pl: 1 }}
            >
              {section.depth > 0 && (
                <TreeConnector ancestorLines={section.ancestorLines} isLast={section.isLast} />
              )}
              <ListItemText
                primary={section.name?.trim() || `Section ${section.id}`}
                secondary={section.description?.trim() || "Aucune description."}
              />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    );
  }

  return (
    <TableContainer>
      <Table size="small" aria-label="Tableau des sections">
        <TableHead>
          <TableRow>
            <TableCell>Nom</TableCell>
            <TableCell>Description</TableCell>
            <TableCell width="120">Niveau</TableCell>
            <TableCell width="48" />
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((section) => (
            <TableRow
              key={section.id}
              hover
              selected={section.id === selectedSectionId}
              onClick={() => onSelect(section.id)}
              sx={{ cursor: "pointer" }}
            >
              <TableCell>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  {section.depth > 0 && (
                    <TreeConnector ancestorLines={section.ancestorLines} isLast={section.isLast} />
                  )}
                  {section.name?.trim() || `Section ${section.id}`}
                </Box>
              </TableCell>
              <TableCell>{section.description?.trim() || "Aucune description."}</TableCell>
              <TableCell>{section.depth + 1}</TableCell>
              <TableCell padding="none">
                {section.id === selectedSectionId && (
                  <Tooltip title="Ajouter un enfant">
                    <IconButton
                      size="small"
                      onClick={(e) => { e.stopPropagation(); onAddChild(section.id); }}
                      aria-label="Ajouter un enfant"
                    >
                      <AddIcon fontSize="small" />
                    </IconButton>
                  </Tooltip>
                )}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export const SectionListPage: FC<SectionListPageProps> = () => {
  const { data, isLoading, error } = useListSectionsQuery();
  const [deleteSection, { isLoading: isDeleting }] = useDeleteSectionByIdMutation();
  const [selectedSectionId, setSelectedSectionId] = useState<string | null>(null);
  const [panelMode, setPanelMode] = useState<PanelMode>("view");
  const [createParentId, setCreateParentId] = useState<string | undefined>(undefined);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("md"));

  const sections = useMemo(
    () => toSectionTree(data?.items),
    [data?.items],
  );

  const sectionRows = useMemo(
    () => flattenSections(sections),
    [sections],
  );

  useEffect(() => {
    if (sectionRows.length === 0) {
      setSelectedSectionId(null);
      return;
    }

    setSelectedSectionId((current) => {
      if (current && sectionRows.some((section) => section.id === current)) {
        return current;
      }

      return sectionRows[0].id;
    });
  }, [sectionRows]);

  const selectedSection = sectionRows.find((section) => section.id === selectedSectionId) ?? null;

  const handleDeleteConfirm = async () => {
    if (!selectedSectionId) return;
    await deleteSection({ id: selectedSectionId });
    setDeleteDialogOpen(false);
  };

  const handleSelectSection = (id: string) => {
    setSelectedSectionId(id);
    setPanelMode((m) => m === "create" ? "view" : m);
  };

  const handleAddChild = (parentId: string) => {
    setCreateParentId(parentId);
    setPanelMode("create");
  };

  let selectedSectionPanel = null;

  if (panelMode === "create") {
    selectedSectionPanel = <SectionCreatePage parentId={createParentId} />;
  } else if (selectedSectionId) {
    if (panelMode === "view") {
      selectedSectionPanel = <SectionDetailPage id={selectedSectionId} onSelectSection={handleSelectSection} />;
    } else {
      selectedSectionPanel = <SectionEditPage id={selectedSectionId} />;
    }
  }

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des sections</div>;

  const navigationHint = isMobile
    ? "Sélectionnez une section dans la liste pour afficher ses informations ou la modifier."
    : "Sélectionnez une section dans le tableau pour afficher ses informations ou la modifier.";

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: "column", sm: "row" }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: "center" } }}>
        <Typography variant="h4" component="h1">
          Sections
        </Typography>
        <Chip label={`${data?.count ?? 0} section${(data?.count ?? 0) > 1 ? "s" : ""}`} color="primary" variant="outlined" />
      </Stack>

      <Box
        sx={{
          display: "grid",
          gap: 3,
          gridTemplateColumns: { xs: "1fr", lg: "320px minmax(0, 1fr)" },
          alignItems: "start",
        }}
      >
        <Paper variant="outlined" sx={{ p: 2, minHeight: 480 }}>
          <Stack spacing={2}>
            <Box>
              <Stack direction="row" spacing={1} sx={{ alignItems: "center", justifyContent: "space-between" }}>
                <Typography variant="h6">Navigation</Typography>
                <Button
                  variant="contained"
                  size="small"
                  onClick={() => { setCreateParentId(undefined); setSelectedSectionId(null); setPanelMode("create"); }}
                >
                  Nouvelle section
                </Button>
              </Stack>
              <Typography variant="body2" color="text.secondary">
                {navigationHint}
              </Typography>
            </Box>
            <Divider />
            <NavigationList sections={sections} selectedSectionId={selectedSectionId} isMobile={isMobile} onSelect={handleSelectSection} onAddChild={handleAddChild} />
          </Stack>
        </Paper>

        <Paper variant="outlined" sx={{ minHeight: 480, overflow: "hidden" }}>
          <Stack spacing={0}>
            <Box sx={{ px: 3, pt: 3, pb: 2 }}>
              <Stack direction="row" spacing={1} sx={{ alignItems: "flex-start", justifyContent: "space-between" }}>
                <Box>
                  <Typography variant="h5">
                    {panelMode === "create" ? "Nouvelle section" : (selectedSection?.name?.trim() || "Aucune section sélectionnée")}
                  </Typography>
                  <PanelSubtitle panelMode={panelMode} selectedSection={selectedSection} />
                </Box>
                {selectedSectionId && (
                  <Button
                    variant="outlined"
                    color="error"
                    size="small"
                    onClick={() => setDeleteDialogOpen(true)}
                    sx={{ whiteSpace: "nowrap", flexShrink: 0 }}
                  >
                    Supprimer
                  </Button>
                )}
              </Stack>
            </Box>

            <Divider />

            <Tabs
              value={panelMode === "create" ? false : panelMode}
              onChange={(_event, nextValue: PanelMode) => setPanelMode(nextValue)}
              aria-label="Panneau de section"
              sx={{ px: 2, pt: 1 }}
            >
              <Tab label="Aperçu" value="view" disabled={!selectedSectionId || panelMode === "create"} />
              <Tab label="Édition" value="edit" disabled={!selectedSectionId || panelMode === "create"} />
            </Tabs>

            <Divider />

            <Box sx={{ p: { xs: 2, md: 3 } }}>
              {selectedSectionPanel}
            </Box>
          </Stack>
        </Paper>
      </Box>

      <Dialog open={deleteDialogOpen} onClose={() => setDeleteDialogOpen(false)}>
        <DialogTitle>Supprimer la section</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Êtes-vous sûr de vouloir supprimer la section{" "}
            <strong>{selectedSection?.name?.trim() || selectedSectionId}</strong> ? Cette action est irréversible.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)} disabled={isDeleting}>
            Annuler
          </Button>
          <Button onClick={handleDeleteConfirm} color="error" variant="contained" disabled={isDeleting}>
            {isDeleting ? "Suppression…" : "Supprimer"}
          </Button>
        </DialogActions>
      </Dialog>
    </Stack>
  );
};
