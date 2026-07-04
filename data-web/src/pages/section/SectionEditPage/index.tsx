import { useEffect } from "react";
import { useForm, FormProvider } from "react-hook-form";
import { useGetSectionByIdQuery, useUpdateSectionMutation } from "../../../services/sectionApi";
import type { FC } from "react";
import type { SectionEditPageProps } from "./SectionEditPage.types";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Paper from "@mui/material/Paper";
import SectionNameField from "../../../components/SectionNameField";
import SectionDescriptionField from "../../../components/SectionDescriptionField";
import SectionDisplayOrderField from "../../../components/SectionDisplayOrderField";
import SectionParentField from "../../../components/SectionParentField";

type SectionEditFormValues = {
  name: string;
  description: string;
  displayOrder: number;
  parentId: string;
};

export const SectionEditPage: FC<SectionEditPageProps> = ({ id }) => {
  const { data, isLoading, error } = useGetSectionByIdQuery({ id });
  const [updateSection, { isLoading: isSaving, error: saveError, isSuccess }] = useUpdateSectionMutation();
  const methods = useForm<SectionEditFormValues>({
    defaultValues: { name: "", description: "", displayOrder: 0, parentId: "" },
  });
  const { handleSubmit, reset } = methods;

  useEffect(() => {
    if (data) {
      reset({
        name: data.name ?? "",
        description: data.description ?? "",
        displayOrder: data.displayOrder ?? 0,
        parentId: data.parentId ?? "",
      });
    }
  }, [data, reset]);

  const onSubmit = async (values: SectionEditFormValues) => {
    await updateSection({
      id,
      section: {
        id,
        name: values.name,
        description: values.description,
        displayOrder: values.displayOrder,
        parentId: values.parentId || undefined,
      },
    });
  };

  if (isLoading) return <div>Chargement...</div>;
  if (error) return <div>Erreur lors du chargement de la section</div>;
  if (!data) return <div>Section introuvable</div>;

  return (
    <Box sx={{ maxWidth: 500, mx: "auto" }}>
      <Paper sx={{ p: 3, mt: 3 }}>
        <h1>Modifier la section</h1>
        <FormProvider {...methods}>
          <form onSubmit={handleSubmit(onSubmit)}>
            <SectionNameField disabled={isSaving} />
            <SectionDescriptionField disabled={isSaving} />
            <SectionDisplayOrderField disabled={isSaving} />
            <SectionParentField disabled={isSaving} excludedSectionId={id} />
            <Button type="submit" variant="contained" color="primary" disabled={isSaving} fullWidth>
              Enregistrer
            </Button>
          </form>
        </FormProvider>
        {isSuccess && <Box sx={{ color: "success.main", mt: 2 }}>Section modifiée !</Box>}
        {saveError && <Box sx={{ color: "error.main", mt: 2 }}>Erreur lors de la modification</Box>}
      </Paper>
    </Box>
  );
};
