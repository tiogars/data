import type { FC } from "react";
import Box from "@mui/material/Box";
import Divider from "@mui/material/Divider";
import Paper from "@mui/material/Paper";
import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";
import { useGetSectionByIdQuery } from "../../../services/sectionApi";
import type { SectionDetailPageProps } from "./SectionDetailPage.types";

export const SectionDetailPage: FC<SectionDetailPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetSectionByIdQuery({ id });

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
      </Stack>
    </Paper>
  );
};
