import { useEffect, useMemo, useState, type FC } from "react";
import Box from "@mui/material/Box";
import Chip from "@mui/material/Chip";
import Divider from "@mui/material/Divider";
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
import Typography from "@mui/material/Typography";
import useMediaQuery from "@mui/material/useMediaQuery";
import { useTheme } from "@mui/material/styles";
import { SectionDetailPage } from "../SectionDetailPage";
import { SectionEditPage } from "../SectionEditPage";
import { useListSectionsQuery, type Section } from "../../../services/sectionApi";
import type { SectionListPageProps } from "./SectionListPage.types";

type PanelMode = "view" | "edit";

export const SectionListPage: FC<SectionListPageProps> = () => {
  const { data, isLoading, error } = useListSectionsQuery();
  const [selectedSectionId, setSelectedSectionId] = useState<string | null>(null);
  const [panelMode, setPanelMode] = useState<PanelMode>("view");
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("md"));

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

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement des sections</div>;

  const navigationHint = isMobile
    ? "Sélectionnez une section dans la liste pour afficher ses informations ou la modifier."
    : "Sélectionnez une section dans le tableau pour afficher ses informations ou la modifier.";

  let navigationContent = <Typography color="text.secondary">Aucune section disponible.</Typography>;

  if (sections.length > 0) {
    if (isMobile) {
      navigationContent = (
        <List sx={{ p: 0 }}>
          {sections.map((section) => {
            const isSelected = section.id === selectedSectionId;
            const sectionName = section.name?.trim() || `Section ${section.id}`;
            const sectionDescription = section.description?.trim() || "Aucune description.";

            return (
              <ListItem key={section.id} disablePadding>
                <ListItemButton
                  selected={isSelected}
                  onClick={() => setSelectedSectionId(section.id)}
                  sx={{ borderRadius: 1 }}
                >
                  <ListItemText
                    primary={sectionName}
                    secondary={sectionDescription}
                  />
                </ListItemButton>
              </ListItem>
            );
          })}
        </List>
      );
    } else {
      navigationContent = (
        <TableContainer>
          <Table size="small" aria-label="Tableau des sections">
            <TableHead>
              <TableRow>
                <TableCell>Nom</TableCell>
                <TableCell>Description</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {sections.map((section) => {
                const isSelected = section.id === selectedSectionId;
                const sectionName = section.name?.trim() || `Section ${section.id}`;
                const sectionDescription = section.description?.trim() || "Aucune description.";

                return (
                  <TableRow
                    key={section.id}
                    hover
                    selected={isSelected}
                    onClick={() => setSelectedSectionId(section.id)}
                    sx={{ cursor: "pointer" }}
                  >
                    <TableCell>{sectionName}</TableCell>
                    <TableCell>{sectionDescription}</TableCell>
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
        </TableContainer>
      );
    }
  }

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
                {navigationHint}
              </Typography>
            </Box>
            <Divider />
            {navigationContent}
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
                  : "Choisissez une section dans la navigation pour commencer."}
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
