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
import Paper from "@mui/material/Paper";
import Stack from "@mui/material/Stack";
import Tab from "@mui/material/Tab";
import TablePagination from "@mui/material/TablePagination";
import TextField from "@mui/material/TextField";
import Tabs from "@mui/material/Tabs";
import Tooltip from "@mui/material/Tooltip";
import Typography from "@mui/material/Typography";
import AddIcon from "@mui/icons-material/Add";
import { SimpleTreeView, TreeItem } from "@mui/x-tree-view";
import { SectionCreatePage } from "../SectionCreatePage";
import { SectionDetailPage } from "../SectionDetailPage";
import { SectionEditPage } from "../SectionEditPage";
import { useSearchSectionsQuery, useDeleteSectionByIdMutation, type Section } from "../../../services/sectionApi";
import type { SectionListPageProps } from "./SectionListPage.types";
import { collectExpandableIds, flattenSections, toSectionTree, type SectionTreeNode } from "./sectionTree";

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

type NavigationListProps = {
  sections: SectionTreeNode[];
  selectedSectionId: string | null;
  onSelect: (id: string) => void;
  onAddChild: (parentId: string) => void;
};

type SectionTreeItemProps = {
  section: SectionTreeNode;
  depth: number;
  selectedSectionId: string | null;
  onAddChild: (parentId: string) => void;
};

const SectionTreeItemLabel: FC<SectionTreeItemProps> = ({ section, depth, selectedSectionId, onAddChild }) => {
  const isSelected = section.id === selectedSectionId;

  return (
    <Stack direction="row" spacing={1} sx={{ alignItems: "flex-start", minWidth: 0, py: 0.25, pr: 0.5 }}>
      <Box sx={{ minWidth: 0, flexGrow: 1 }}>
        <Typography
          variant="body2"
          sx={{
            fontWeight: isSelected ? 700 : 500,
            overflow: "hidden",
            textOverflow: "ellipsis",
            whiteSpace: "nowrap",
          }}
        >
          {section.name?.trim() || `Section ${section.id}`}
        </Typography>
        <Typography
          variant="caption"
          color="text.secondary"
          sx={{
            display: "block",
            overflow: "hidden",
            textOverflow: "ellipsis",
            whiteSpace: "nowrap",
          }}
        >
          {section.description?.trim() || "Aucune description."} · Niveau {depth + 1}
        </Typography>
      </Box>
      {isSelected && (
        <Tooltip title="Ajouter un enfant">
          <IconButton
            size="small"
            aria-label="Ajouter un enfant"
            onMouseDown={(event) => {
              event.preventDefault();
              event.stopPropagation();
            }}
            onClick={(event) => {
              event.preventDefault();
              event.stopPropagation();
              onAddChild(section.id);
            }}
          >
            <AddIcon fontSize="small" />
          </IconButton>
        </Tooltip>
      )}
    </Stack>
  );
};

const renderTreeItems = (
  sections: SectionTreeNode[],
  selectedSectionId: string | null,
  onAddChild: (parentId: string) => void,
  depth = 0,
): React.ReactNode => sections.map((section) => (
  <TreeItem
    key={section.id}
    itemId={section.id}
    label={<SectionTreeItemLabel section={section} depth={depth} selectedSectionId={selectedSectionId} onAddChild={onAddChild} />}
  >
    {renderTreeItems(section.children, selectedSectionId, onAddChild, depth + 1)}
  </TreeItem>
));

const NavigationList: FC<NavigationListProps> = ({ sections, selectedSectionId, onSelect, onAddChild }) => {
  const [expandedSectionIds, setExpandedSectionIds] = useState<readonly string[]>([]);
  const defaultExpandedSectionIds = useMemo(() => collectExpandableIds(sections), [sections]);

  useEffect(() => {
    setExpandedSectionIds(defaultExpandedSectionIds);
  }, [defaultExpandedSectionIds]);

  if (sections.length === 0) return <Typography color="text.secondary">Aucune section disponible.</Typography>;

  return (
    <SimpleTreeView
      aria-label="Arborescence des sections"
      selectedItems={selectedSectionId ?? undefined}
      onSelectedItemsChange={(_event, itemId) => {
        if (typeof itemId === "string") {
          onSelect(itemId);
        }
      }}
      expandedItems={expandedSectionIds}
      onExpandedItemsChange={(_event, itemIds) => setExpandedSectionIds(itemIds)}
      expansionTrigger="iconContainer"
      itemChildrenIndentation={20}
      sx={{
        overflowX: "hidden",
        '& .MuiTreeItem-content': {
          borderRadius: 1,
          py: 0.25,
        },
        '& .MuiTreeItem-content.Mui-selected, & .MuiTreeItem-content.Mui-selected.Mui-focused': {
          backgroundColor: "action.selected",
        },
      }}
    >
      {renderTreeItems(sections, selectedSectionId, onAddChild)}
    </SimpleTreeView>
  );
};

export const SectionListPage: FC<SectionListPageProps> = () => {
  const [searchInput, setSearchInput] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(20);
  const queryArgs = useMemo(() => ({
    page,
    size: pageSize,
    q: searchQuery || undefined,
  }), [page, pageSize, searchQuery]);
  const { data, isLoading, error, refetch } = useSearchSectionsQuery(queryArgs, { refetchOnMountOrArgChange: true });
  const [deleteSection, { isLoading: isDeleting }] = useDeleteSectionByIdMutation();
  const [selectedSectionId, setSelectedSectionId] = useState<string | null>(null);
  const [panelMode, setPanelMode] = useState<PanelMode>("view");
  const [createParentId, setCreateParentId] = useState<string | undefined>(undefined);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);

  const sections = useMemo(
    () => toSectionTree(data?.items),
    [data?.items],
  );

  const sectionRows = useMemo(
    () => flattenSections(sections),
    [sections],
  );

  useEffect(() => {
    const timeout = setTimeout(() => {
      setSearchQuery(searchInput.trim());
      setPage(0);
    }, 300);
    return () => clearTimeout(timeout);
  }, [searchInput]);

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
    const parentSectionId = selectedSection?.parentId ?? null;

    await deleteSection({ id: selectedSectionId }).unwrap();
    await refetch();

    setSelectedSectionId(parentSectionId);
    setPanelMode("view");
    setDeleteDialogOpen(false);
  };

  const handleSectionCreated = async (createdSectionId: string, parentSectionId?: string) => {
    await refetch();
    setCreateParentId(parentSectionId);
    setSelectedSectionId(createdSectionId);
    setPanelMode("view");
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
    selectedSectionPanel = <SectionCreatePage parentId={createParentId} onCreated={handleSectionCreated} />;
  } else if (selectedSectionId) {
    if (panelMode === "view") {
      selectedSectionPanel = <SectionDetailPage id={selectedSectionId} onSelectSection={handleSelectSection} />;
    } else {
      selectedSectionPanel = <SectionEditPage id={selectedSectionId} />;
    }
  }

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des sections</div>;

  const navigationHint = "Sélectionnez une section dans l'arborescence pour afficher ses informations ou la modifier.";

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: "column", sm: "row" }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: "center" } }}>
        <Typography variant="h4" component="h1">
          Sections
        </Typography>
        <Chip label={`${data?.count ?? 0} section${(data?.count ?? 0) > 1 ? "s" : ""}`} color="primary" variant="outlined" />
      </Stack>

      <TextField
        fullWidth
        label="Recherche"
        placeholder="Rechercher par nom ou description"
        value={searchInput}
        onChange={(event) => setSearchInput(event.target.value)}
      />

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
            <NavigationList sections={sections} selectedSectionId={selectedSectionId} onSelect={handleSelectSection} onAddChild={handleAddChild} />
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

      <TablePagination
        component="div"
        count={data?.count ?? 0}
        page={page}
        onPageChange={(_event, nextPage) => setPage(nextPage)}
        rowsPerPage={pageSize}
        onRowsPerPageChange={(event) => {
          const nextSize = Number(event.target.value);
          setPageSize(nextSize);
          setPage(0);
        }}
        rowsPerPageOptions={[10, 20, 50]}
      />

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
