import { useEffect, useMemo, useState, type FC, type SyntheticEvent } from "react";
import Box from "@mui/material/Box";
import Chip from "@mui/material/Chip";
import Divider from "@mui/material/Divider";
import Paper from "@mui/material/Paper";
import Stack from "@mui/material/Stack";
import Tab from "@mui/material/Tab";
import Tabs from "@mui/material/Tabs";
import Typography from "@mui/material/Typography";
import { SimpleTreeView } from "@mui/x-tree-view/SimpleTreeView";
import { TreeItem } from "@mui/x-tree-view/TreeItem";
import { SectionDetailPage } from "../SectionDetailPage";
import { SectionEditPage } from "../SectionEditPage";
import { useListSectionsQuery, type Section } from "../../../services/sectionApi";
import type { SectionListPageProps } from "./SectionListPage.types";

type PanelMode = "view" | "edit";

export const SectionListPage: FC<SectionListPageProps> = () => {
  const { data, isLoading, error } = useListSectionsQuery();
  const [selectedSectionId, setSelectedSectionId] = useState<string | null>(null);
  const [panelMode, setPanelMode] = useState<PanelMode>("view");

  const sections = useMemo(
    () => (data?.items ?? []).filter((section): section is Section & { id: string } => Boolean(section.id)),
    [data?.items],
  );

  useEffect(() => {
    if (sections.length === 0) {
      setSelectedSectionId(null);
      return;
    }

    setSelectedSectionId((current) => {
      if (current && sections.some((section) => section.id === current)) {
        return current;
      }

      return sections[0].id;
    });
  }, [sections]);

  const selectedSection = sections.find((section) => section.id === selectedSectionId) ?? null;
  let selectedSectionPanel = null;

  if (selectedSectionId) {
    if (panelMode === "view") {
      selectedSectionPanel = <SectionDetailPage id={selectedSectionId} />;
    } else {
      selectedSectionPanel = <SectionEditPage id={selectedSectionId} />;
    }
  }

  const handleSelectedItemsChange = (
    _event: SyntheticEvent | null,
    itemIds: string | string[] | null,
  ) => {
    const nextSelection = Array.isArray(itemIds) ? itemIds[0] : itemIds;

    if (!nextSelection || nextSelection === "sections-root") {
      return;
    }

    setSelectedSectionId(nextSelection);
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des sections</div>;

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack direction={{ xs: "column", sm: "row" }} spacing={1.5} useFlexGap sx={{ alignItems: { sm: "center" } }}>
        <Typography variant="h4" component="h1">
          Liste des sections
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
              <Typography variant="h6">Navigation</Typography>
              <Typography variant="body2" color="text.secondary">
                Sélectionnez une section pour afficher ses informations ou la modifier.
              </Typography>
            </Box>
            <Divider />
            {sections.length === 0 ? (
              <Typography color="text.secondary">Aucune section disponible.</Typography>
            ) : (
              <SimpleTreeView
                defaultExpandedItems={["sections-root"]}
                selectedItems={selectedSectionId}
                onSelectedItemsChange={handleSelectedItemsChange}
                sx={{ overflowX: "auto" }}
              >
                <TreeItem itemId="sections-root" label={`Sections (${sections.length})`} disableSelection>
                  {sections.map((section) => (
                    <TreeItem
                      key={section.id}
                      itemId={section.id}
                      label={section.name?.trim() || `Section ${section.id}`}
                    />
                  ))}
                </TreeItem>
              </SimpleTreeView>
            )}
          </Stack>
        </Paper>

        <Paper variant="outlined" sx={{ minHeight: 480, overflow: "hidden" }}>
          <Stack spacing={0}>
            <Box sx={{ px: 3, pt: 3, pb: 2 }}>
              <Typography variant="h5">
                {selectedSection?.name?.trim() || "Aucune section sélectionnée"}
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
                {selectedSection
                  ? selectedSection.description?.trim() || "Cette section n'a pas encore de description."
                  : "Choisissez un élément dans l'arbre à gauche pour commencer."}
              </Typography>
            </Box>

            <Divider />

            <Tabs
              value={panelMode}
              onChange={(_event, nextValue: PanelMode) => setPanelMode(nextValue)}
              aria-label="Panneau de section"
              sx={{ px: 2, pt: 1 }}
            >
              <Tab label="Aperçu" value="view" disabled={!selectedSectionId} />
              <Tab label="Édition" value="edit" disabled={!selectedSectionId} />
            </Tabs>

            <Divider />

            <Box sx={{ p: { xs: 2, md: 3 } }}>
              {selectedSectionPanel}
            </Box>
          </Stack>
        </Paper>
      </Box>
    </Stack>
  );
};
