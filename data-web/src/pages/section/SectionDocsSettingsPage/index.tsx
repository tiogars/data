import { useEffect, useMemo, useState, type FC } from "react";
import Alert from "@mui/material/Alert";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Chip from "@mui/material/Chip";
import Stack from "@mui/material/Stack";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import { useListSectionsQuery, type Section } from "../../../services/sectionApi";
import {
  useGetSectionDocsSettingsStateQuery,
  useUpdateSectionDocsSettingsStateMutation,
  type SectionDocsSetting,
} from "../../../services/sectionDocsSettingsApi";

type RootSection = Section & { id: string };
type DraftSectionDocsSetting = {
  id?: string;
  sectionId: string;
  storagePath: string;
};

function toRootSections(items: Section[] | undefined): RootSection[] {
  return (items ?? [])
    .filter((item): item is RootSection => Boolean(item.id) && !item.parentId)
    .sort((left, right) => {
      const orderComparison = (left.displayOrder ?? 0) - (right.displayOrder ?? 0);
      if (orderComparison !== 0) {
        return orderComparison;
      }
      return (left.name ?? "").localeCompare(right.name ?? "", "fr");
    });
}

export const SectionDocsSettingsPage: FC = () => {
  const { data: sectionsData, isLoading: isSectionsLoading, error: sectionsError } = useListSectionsQuery(undefined, {
    refetchOnMountOrArgChange: true,
  });
  const { data: settingsData, isLoading: isSettingsLoading, error: settingsError, refetch } = useGetSectionDocsSettingsStateQuery(undefined, {
    refetchOnMountOrArgChange: true,
  });
  const [updateState, { isLoading: isSaving }] = useUpdateSectionDocsSettingsStateMutation();
  const [draftPaths, setDraftPaths] = useState<Record<string, string>>({});

  const rootSections = useMemo(() => toRootSections(sectionsData?.items), [sectionsData?.items]);
  const settingsBySectionId = useMemo(() => {
    const entries = new Map<string, SectionDocsSetting>();
    for (const item of settingsData?.items ?? []) {
      if (item.sectionId) {
        entries.set(item.sectionId, item);
      }
    }
    return entries;
  }, [settingsData?.items]);

  useEffect(() => {
    const nextDrafts: Record<string, string> = {};
    for (const section of rootSections) {
      nextDrafts[section.id] = settingsBySectionId.get(section.id)?.storagePath ?? "";
    }
    setDraftPaths(nextDrafts);
  }, [rootSections, settingsBySectionId]);

  const configuredCount = useMemo(
    () => Object.values(draftPaths).filter((value) => value.trim().length > 0).length,
    [draftPaths],
  );

  const handleSave = async () => {
    const items = rootSections.reduce<DraftSectionDocsSetting[]>((accumulator, section) => {
      const path = draftPaths[section.id]?.trim() ?? "";
      if (!path) {
        return accumulator;
      }

      const existing = settingsBySectionId.get(section.id);

      accumulator.push({
        id: existing?.id,
        sectionId: section.id,
        storagePath: path,
      });

      return accumulator;
    }, []);

    await updateState({ sectionDocsSettingsState: { items } }).unwrap();
    await refetch();
  };

  if (isSectionsLoading || isSettingsLoading) {
    return <div>Chargement...</div>;
  }

  if (sectionsError || settingsError) {
    return <div>Erreur lors du chargement des paramètres de documentation.</div>;
  }

  return (
    <Stack spacing={3} sx={{ p: { xs: 2, md: 3 } }}>
      <Stack
        direction={{ xs: "column", sm: "row" }}
        spacing={1.5}
        sx={{ alignItems: { sm: "center" }, justifyContent: "space-between" }}
      >
        <Box>
          <Typography variant="h4" component="h1">
            Paramètres docs des sections
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Associez un chemin relatif sous volumes/docs à chaque section racine.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: "wrap" }}>
          <Chip label={`${configuredCount} configuration${configuredCount > 1 ? "s" : ""}`} color="primary" variant="outlined" />
          <Button variant="contained" onClick={handleSave} disabled={isSaving}>
            {isSaving ? "Enregistrement…" : "Enregistrer"}
          </Button>
        </Stack>
      </Stack>

      <Alert severity="info">
        Utilisez un chemin relatif, par exemple <strong>guides/produits</strong>. Les sections enfants ne sont pas configurables ici.
      </Alert>

      {rootSections.length === 0 && (
        <Alert severity="warning">Aucune section racine disponible pour le paramétrage.</Alert>
      )}

      <Stack spacing={1.5}>
        {rootSections.map((section) => (
          <Card key={section.id} variant="outlined">
            <CardContent>
              <Stack direction={{ xs: "column", md: "row" }} spacing={2} sx={{ justifyContent: "space-between" }}>
                <Box sx={{ minWidth: 0, flex: 1 }}>
                  <Typography variant="subtitle1" sx={{ fontWeight: 600 }}>
                    {(section.displayOrder ?? 0)}. {section.name ?? "Sans nom"}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {section.description?.trim() || "Aucune description fournie."}
                  </Typography>
                </Box>
                <TextField
                  label="Chemin relatif"
                  value={draftPaths[section.id] ?? ""}
                  onChange={(event) => {
                    const nextValue = event.target.value;
                    setDraftPaths((previous) => ({ ...previous, [section.id]: nextValue }));
                  }}
                  placeholder="guides/produits"
                  fullWidth
                  sx={{ maxWidth: { md: 360 } }}
                />
              </Stack>
            </CardContent>
          </Card>
        ))}
      </Stack>
    </Stack>
  );
};