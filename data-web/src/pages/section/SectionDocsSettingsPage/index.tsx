import { useMemo, useState, type FC } from "react";
import Alert from "@mui/material/Alert";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Chip from "@mui/material/Chip";
import IconButton from "@mui/material/IconButton";
import Stack from "@mui/material/Stack";
import TextField from "@mui/material/TextField";
import Typography from "@mui/material/Typography";
import {
  useCreateSectionDocumentMutation,
  useDeleteSectionDocumentMutation,
  useListSectionDocumentsQuery,
  useUpdateSectionDocumentMutation,
} from "../../../services/sectionDocumentApi";
import DeleteIcon from "@mui/icons-material/Delete";

export const SectionDocsSettingsPage: FC = () => {
  const { data: documentsData, isLoading, error, refetch } = useListSectionDocumentsQuery(undefined, {
    refetchOnMountOrArgChange: true,
  });
  const [createDocument, { isLoading: isCreating }] = useCreateSectionDocumentMutation();
  const [updateDocument, { isLoading: isUpdating }] = useUpdateSectionDocumentMutation();
  const [deleteDocument, { isLoading: isDeleting }] = useDeleteSectionDocumentMutation();

  const [newName, setNewName] = useState("");
  const [newPath, setNewPath] = useState("");
  const [drafts, setDrafts] = useState<Record<string, { name: string; storagePath: string }>>({});

  const documents = useMemo(
    () => (documentsData?.items ?? []).filter((item): item is { id: string; name?: string; storagePath?: string } => Boolean(item.id)),
    [documentsData?.items],
  );

  const isSaving = isCreating || isUpdating || isDeleting;

  const configuredCount = useMemo(
    () => documents.length,
    [documents.length],
  );

  const handleCreate = async () => {
    await createDocument({ sectionDocument: { name: newName.trim(), storagePath: newPath.trim() } }).unwrap();
    setNewName("");
    setNewPath("");
    await refetch();
  };

  const handleUpdate = async (id: string) => {
    const currentDraft = drafts[id];
    if (!currentDraft) return;

    await updateDocument({
      id,
      sectionDocument: {
        name: currentDraft.name.trim(),
        storagePath: currentDraft.storagePath.trim(),
      },
    }).unwrap();

    await refetch();
  };

  const handleDelete = async (id: string) => {
    await deleteDocument({ id }).unwrap();
    await refetch();
  };

  if (isLoading) {
    return <div>Chargement...</div>;
  }

  if (error) {
    return <div>Erreur lors du chargement des documents.</div>;
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
            Paramètres documents
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Créez les documents (nom + chemin relatif) qui serviront ensuite de contexte dans la page Sections.
          </Typography>
        </Box>
        <Stack direction="row" spacing={1} useFlexGap sx={{ flexWrap: "wrap" }}>
          <Chip label={`${configuredCount} document${configuredCount > 1 ? "s" : ""}`} color="primary" variant="outlined" />
        </Stack>
      </Stack>

      <Alert severity="info">
        Utilisez un chemin relatif, par exemple <strong>guides/produits</strong>. La page Sections demandera ensuite de choisir un document avant d’afficher l’arbre.
      </Alert>

      <Card variant="outlined">
        <CardContent>
          <Stack direction={{ xs: "column", md: "row" }} spacing={2} sx={{ alignItems: { md: "center" } }}>
            <TextField
              label="Nom du document"
              value={newName}
              onChange={(event) => setNewName(event.target.value)}
              fullWidth
            />
            <TextField
              label="Chemin relatif"
              value={newPath}
              onChange={(event) => setNewPath(event.target.value)}
              placeholder="guides/produits"
              fullWidth
            />
            <Button
              variant="contained"
              onClick={handleCreate}
              disabled={isSaving || !newName.trim() || !newPath.trim()}
            >
              {isCreating ? "Création…" : "Créer"}
            </Button>
          </Stack>
        </CardContent>
      </Card>

      <Stack spacing={1.5}>
        {documents.map((document) => {
          const draft = drafts[document.id] ?? {
            name: document.name ?? "",
            storagePath: document.storagePath ?? "",
          };

          return (
          <Card key={document.id} variant="outlined">
            <CardContent>
              <Stack direction={{ xs: "column", md: "row" }} spacing={2} sx={{ justifyContent: "space-between" }}>
                <Box sx={{ minWidth: 0, flex: 1 }}>
                  <TextField
                    label="Nom"
                    value={draft.name}
                    onChange={(event) => {
                      const value = event.target.value;
                      setDrafts((previous) => ({
                        ...previous,
                        [document.id]: { ...draft, name: value },
                      }));
                    }}
                    fullWidth
                  />
                </Box>
                <TextField
                  label="Chemin relatif"
                  value={draft.storagePath}
                  onChange={(event) => {
                    const nextValue = event.target.value;
                    setDrafts((previous) => ({
                      ...previous,
                      [document.id]: { ...draft, storagePath: nextValue },
                    }));
                  }}
                  placeholder="guides/produits"
                  fullWidth
                  sx={{ maxWidth: { md: 360 } }}
                />
                <Stack direction="row" spacing={1}>
                  <Button
                    variant="outlined"
                    onClick={() => handleUpdate(document.id)}
                    disabled={isSaving || !draft.name.trim() || !draft.storagePath.trim()}
                  >
                    {isUpdating ? "Maj…" : "Mettre à jour"}
                  </Button>
                  <IconButton aria-label="Supprimer document" color="error" onClick={() => handleDelete(document.id)} disabled={isSaving}>
                    <DeleteIcon />
                  </IconButton>
                </Stack>
              </Stack>
            </CardContent>
          </Card>
          );
        })}
      </Stack>
    </Stack>
  );
};