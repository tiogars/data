import { useFormContext, useController } from "react-hook-form";

export function useSectionFormControllers() {
  const { control } = useFormContext();
  const nameController = useController({ name: "name", control, rules: { required: "Le nom est requis" } });
  const descriptionController = useController({ name: "description", control, rules: { required: "La description est requise" } });
  const displayOrderController = useController({
    name: "displayOrder",
    control,
    defaultValue: 0,
    rules: {
      required: "L'ordre d'affichage est requis",
      validate: (value) => Number.isInteger(Number(value)) && Number(value) >= 0
        ? true
        : "L'ordre d'affichage doit être un entier positif ou nul",
    },
  });
  const parentIdController = useController({ name: "parentId", control });
  return { nameController, descriptionController, displayOrderController, parentIdController };
}
