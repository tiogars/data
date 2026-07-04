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
import Select from "@mui/material/Select";
import Stack from "@mui/material/Stack";
import Tab from "@mui/material/Tab";
import TextField from "@mui/material/TextField";
import Tabs from "@mui/material/Tabs";
import Tooltip from "@mui/material/Tooltip";
import Typography from "@mui/material/Typography";
import MenuItem from "@mui/material/MenuItem";
import AddIcon from "@mui/icons-material/Add";
import { SimpleTreeView, TreeItem } from "@mui/x-tree-view";
import { Link as RouterLink } from "react-router-dom";
import { SectionCreatePage } from "../SectionCreatePage";
import { SectionDetailPage } from "../SectionDetailPage";
import { SectionEditPage } from "../SectionEditPage";
import { useDeleteSectionByIdMutation, useListSectionsQuery, type Section } from "../../../services/sectionApi";
import { useListSectionDocumentsQuery } from "../../../services/sectionDocumentApi";
import type { SectionListPageProps } from "./SectionListPage.types";
import {
  collectExpandableIds,
  collectTreeIds,
  filterSectionTree,
  findSectionPath,
  flattenSections,
  formatSectionOrder,
  toSectionTree,
  type SectionTreeNode,
} from "./sectionTree";

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
  allSections: SectionTreeNode[];
  selectedSectionId: string | null;
  searchQuery: string;
  onSelect: (id: string) => void;
  onAddChild: (parentId: string) => void;
};

type SectionTreeItemProps = {
  section: SectionTreeNode;
  indexPrefix: string;
  selectedSectionId: string | null;
  onAddChild: (parentId: string) => void;
};

const SectionTreeItemLabel: FC<SectionTreeItemProps> = ({ section, indexPrefix, selectedSectionId, onAddChild }) => {
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
          {indexPrefix} {section.name?.trim() || `Section ${section.id}`}
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
  parentPrefix = "",
): React.ReactNode => sections.map((section) => {
  const sectionPrefix = parentPrefix
    ? `${parentPrefix}.${formatSectionOrder(section)}`
    : formatSectionOrder(section);

  return (
  <TreeItem
    key={section.id}
    itemId={section.id}
    label={<SectionTreeItemLabel section={section} indexPrefix={sectionPrefix} selectedSectionId={selectedSectionId} onAddChild={onAddChild} />}
  >
    {renderTreeItems(section.children, selectedSectionId, onAddChild, sectionPrefix)}
  </TreeItem>
  );
});

const NavigationList: FC<NavigationListProps> = ({ sections, allSections, selectedSectionId, searchQuery, onSelect, onAddChild }) => {
  const [expandedSectionIds, setExpandedSectionIds] = useState<readonly string[]>([]);
  const defaultExpandedSectionIds = useMemo(() => collectExpandableIds(allSections), [allSections]);
  const allTreeIds = useMemo(() => collectTreeIds(allSections), [allSections]);

  const selectedSectionAncestors = useMemo(() => {
    if (!selectedSectionId) return [];
    const path = findSectionPath(allSections, selectedSectionId);
    return path.slice(0, -1);
  }, [allSections, selectedSectionId]);

  useEffect(() => {
    setExpandedSectionIds((current) => {
      const expandedFromCurrent = current.filter((id) => allTreeIds.includes(id));

      if (searchQuery.trim()) {
        return defaultExpandedSectionIds;
      }

      const nextExpanded = new Set<string>(expandedFromCurrent);
      for (const ancestorId of selectedSectionAncestors) {
        nextExpanded.add(ancestorId);
      }

      if (nextExpanded.size === 0) {
        return defaultExpandedSectionIds;
      }

      return Array.from(nextExpanded);
    });
  }, [allTreeIds, defaultExpandedSectionIds, searchQuery, selectedSectionAncestors]);

  if (sections.length === 0) return <Typography color="text.secondary">Aucune section disponible.</Typography>;

  return (
    <Stack spacing={1.5}>
      <Stack direction="row" spacing={1} sx={{ alignItems: "center", justifyContent: "flex-end", flexWrap: "wrap" }}>
        <Button size="small" onClick={() => setExpandedSectionIds(defaultExpandedSectionIds)}>
          Tout déplier
        </Button>
        <Button size="small" onClick={() => setExpandedSectionIds([])}>
          Tout réduire
        </Button>
      </Stack>

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
        itemChildrenIndentation={24}
        sx={{
          overflowX: "hidden",
          border: 1,
          borderColor: "divider",
          borderRadius: 1,
          px: 0.5,
          py: 0.75,
          bgcolor: "background.paper",
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
    </Stack>
  );
};

export const SectionListPage: FC<SectionListPageProps> = () => {
  const [searchInput, setSearchInput] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const { data: documentsData, isLoading: isDocumentsLoading, error: documentsError } = useListSectionDocumentsQuery(undefined, {
    refetchOnMountOrArgChange: true,
  });
  const [selectedDocumentId, setSelectedDocumentId] = useState<string>("");

  const { data, isLoading, error, refetch } = useListSectionsQuery(
    { documentId: selectedDocumentId || undefined },
    { refetchOnMountOrArgChange: true, skip: !selectedDocumentId },
  );
  const [deleteSection, { isLoading: isDeleting }] = useDeleteSectionByIdMutation();
  const [selectedSectionId, setSelectedSectionId] = useState<string | null>(null);
  const [panelMode, setPanelMode] = useState<PanelMode>("view");
  const [createParentId, setCreateParentId] = useState<string | undefined>(undefined);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);

  const documentOptions = useMemo(
    () => (documentsData?.items ?? []).filter((document): document is { id: string; name?: string } => Boolean(document.id)),
    [documentsData?.items],
  );

  useEffect(() => {
    if (documentOptions.length === 0) {
      setSelectedDocumentId("");
      return;
    }

    setSelectedDocumentId((current) => {
      if (current && documentOptions.some((document) => document.id === current)) {
        return current;
      }

      return documentOptions[0].id;
    });
  }, [documentOptions]);

  const sections = useMemo(
    () => toSectionTree(data?.items),
    [data?.items],
  );

  const filteredSections = useMemo(
    () => filterSectionTree(sections, searchQuery),
    [sections, searchQuery],
  );

  const sectionRows = useMemo(
    () => flattenSections(filteredSections),
    [filteredSections],
  );

  useEffect(() => {
    const timeout = setTimeout(() => {
      setSearchQuery(searchInput.trim());
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
    selectedSectionPanel = selectedDocumentId
      ? <SectionCreatePage documentId={selectedDocumentId} parentId={createParentId} onCreated={handleSectionCreated} />
      : null;
  } else if (selectedSectionId) {
    if (panelMode === "view") {
      selectedSectionPanel = <SectionDetailPage id={selectedSectionId} onSelectSection={handleSelectSection} />;
    } else {
      selectedSectionPanel = <SectionEditPage id={selectedSectionId} />;
    }
  }

  if (isDocumentsLoading || isLoading) return <div>Chargement...</div>;
  if (documentsError || error) return <div>Erreur lors du chargement des sections</div>;

  const navigationHint = "Sélectionnez une section dans l'arborescence pour afficher ses informations ou la modifier.";

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: "column", sm: "row" }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: "center" } }}>
        <Typography variant="h4" component="h1">
          Sections
        </Typography>
        <Chip label={`${data?.count ?? 0} section${(data?.count ?? 0) > 1 ? "s" : ""}`} color="primary" variant="outlined" />
      </Stack>

      <Stack direction={{ xs: "column", md: "row" }} spacing={2}>
        <Select
          size="small"
          value={selectedDocumentId}
          onChange={(event) => {
            setSelectedDocumentId(event.target.value);
            setSelectedSectionId(null);
            setPanelMode("view");
            setCreateParentId(undefined);
          }}
          displayEmpty
          sx={{ minWidth: 260 }}
        >
          {documentOptions.length === 0 && (
            <MenuItem value="" disabled>
              Aucun document
            </MenuItem>
          )}
          {documentOptions.map((document) => (
            <MenuItem key={document.id} value={document.id}>
              {document.name?.trim() || "Document sans nom"}
            </MenuItem>
          ))}
        </Select>
        <Button component={RouterLink} to="/section/settings/docs" variant="outlined" size="small" sx={{ alignSelf: { md: "center" } }}>
          Gérer les documents
        </Button>
      </Stack>

      <TextField
        fullWidth
        label="Recherche"
        placeholder="Rechercher par nom ou description"
        value={searchInput}
        onChange={(event) => setSearchInput(event.target.value)}
        disabled={!selectedDocumentId}
      />

      {!selectedDocumentId && (
        <Paper variant="outlined" sx={{ p: 2 }}>
          <Typography color="text.secondary">
            Créez un document dans Paramètres docs, puis sélectionnez-le pour charger l'arbre des sections.
          </Typography>
        </Paper>
      )}

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
                <Stack direction="row" spacing={1}>
                  <Button
                    variant="contained"
                    size="small"
                    disabled={!selectedDocumentId}
                    onClick={() => { setCreateParentId(undefined); setSelectedSectionId(null); setPanelMode("create"); }}
                  >
                    Nouvelle section
                  </Button>
                </Stack>
              </Stack>
              <Typography variant="body2" color="text.secondary">
                {navigationHint}
              </Typography>
            </Box>
            <Divider />
            <NavigationList
              sections={filteredSections}
              allSections={sections}
              selectedSectionId={selectedSectionId}
              searchQuery={searchQuery}
              onSelect={handleSelectSection}
              onAddChild={handleAddChild}
            />
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
