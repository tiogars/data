import { useForm, FormProvider } from "react-hook-form";
import { useCreateSectionMutation } from "../../../services/sectionApi";
import type { FC } from "react";
import type { SectionCreatePageProps } from "./SectionCreatePage.types";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Paper from "@mui/material/Paper";
import SectionNameField from "../../../components/SectionNameField";
import SectionDescriptionField from "../../../components/SectionDescriptionField";

export const SectionCreatePage: FC<SectionCreatePageProps> = () => {
  const [createSection, { isLoading, error, isSuccess }] = useCreateSectionMutation();
  const methods = useForm({
    defaultValues: { name: "", description: "" },
  });
  const { handleSubmit, reset } = methods;

  const onSubmit = async (values: { name: string; description: string }) => {
    await createSection({ sectionCreationForm: values });
    reset();
  };

  return (
    <Box sx={{ maxWidth: 500, mx: "auto" }}>
      <Paper sx={{ p: 3, mt: 3 }}>
        <h1>Créer une section</h1>
        <FormProvider {...methods}>
          <form onSubmit={handleSubmit(onSubmit)}>
            <SectionNameField disabled={isLoading} />
            <SectionDescriptionField disabled={isLoading} />
            <Button type="submit" variant="contained" color="primary" disabled={isLoading} fullWidth>
              Créer
            </Button>
          </form>
        </FormProvider>
        {isSuccess && <Box sx={{ color: "success.main", mt: 2 }}>Section créée !</Box>}
        {error && <Box sx={{ color: "error.main", mt: 2 }}>Erreur lors de la création</Box>}
      </Paper>
    </Box>
  );
};
