import { useEffect } from "react";
import { useForm, FormProvider } from "react-hook-form";
import { useCreateSectionMutation } from "../../../services/sectionApi";
import type { FC } from "react";
import type { SectionCreatePageProps } from "./SectionCreatePage.types";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Paper from "@mui/material/Paper";
import SectionNameField from "../../../components/SectionNameField";
import SectionDescriptionField from "../../../components/SectionDescriptionField";
import SectionDisplayOrderField from "../../../components/SectionDisplayOrderField";
import SectionParentField from "../../../components/SectionParentField";

type SectionCreateFormValues = {
  name: string;
  description: string;
  displayOrder: number;
  parentId: string;
};

export const SectionCreatePage: FC<SectionCreatePageProps> = ({ parentId, onCreated }) => {
  const [createSection, { isLoading, error, isSuccess }] = useCreateSectionMutation();
  const methods = useForm<SectionCreateFormValues>({
    defaultValues: { name: "", description: "", displayOrder: 0, parentId: parentId ?? "" },
  });
  const { handleSubmit, reset } = methods;

  useEffect(() => {
    reset({ name: "", description: "", displayOrder: 0, parentId: parentId ?? "" });
  }, [parentId, reset]);

  const onSubmit = async (values: SectionCreateFormValues) => {
    const createdSection = await createSection({
      sectionCreationForm: {
        name: values.name,
        description: values.description,
        displayOrder: values.displayOrder,
        parentId: values.parentId || undefined,
      },
    }).unwrap();

    if (createdSection.id) {
      await onCreated?.(createdSection.id, createdSection.parentId);
    }

    reset({ name: "", description: "", displayOrder: 0, parentId: parentId ?? "" });
  };

  return (
    <Box sx={{ maxWidth: 500, mx: "auto" }}>
      <Paper sx={{ p: 3, mt: 3 }}>
        <h1>Créer une section</h1>
        <FormProvider {...methods}>
          <form onSubmit={handleSubmit(onSubmit)}>
            <SectionNameField disabled={isLoading} />
            <SectionDescriptionField disabled={isLoading} />
            <SectionDisplayOrderField disabled={isLoading} />
            <SectionParentField disabled={isLoading} />
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
